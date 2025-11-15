package com.example.spotfinder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotfinder.data.SpotFinderDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private lateinit var addressInput: EditText
    private lateinit var searchButton: Button
    private lateinit var zoomInButton: Button
    private lateinit var zoomOutButton: Button

    private lateinit var manageButton: Button

    private val dao by lazy { SpotFinderDatabase.getDatabase(this).locationDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Views
        addressInput = findViewById(R.id.addressInput)
        searchButton = findViewById(R.id.searchButton)
        zoomInButton = findViewById(R.id.zoomInButton)
        zoomOutButton = findViewById(R.id.zoomOutButton)
        manageButton = findViewById(R.id.manageLocationsButton)

        manageButton.setOnClickListener {
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }

        // Map fragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Search by address
        searchButton.setOnClickListener {
            val query = addressInput.text.toString().trim()
            if (query.isEmpty()) {
                Toast.makeText(this, "Please enter an address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val location = dao.getLocationByAddress(query)
                if (location != null) {
                    val coords = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(coords, 12f))
                } else {
                    Toast.makeText(this@MainActivity, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Zoom controls
        zoomInButton.setOnClickListener { map.animateCamera(CameraUpdateFactory.zoomIn()) }
        zoomOutButton.setOnClickListener { map.animateCamera(CameraUpdateFactory.zoomOut()) }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerClickListener(this)

        // Default camera over GTA
        val gta = LatLng(43.7, -79.42)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(gta, 10f))

        // Custom info window to show ID, latitude, and longitude
        map.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker): android.view.View? {
                val info = layoutInflater.inflate(R.layout.marker_window, null)
                val title = info.findViewById<android.widget.TextView>(R.id.markerTitle)
                val details = info.findViewById<android.widget.TextView>(R.id.markerContent)
                title.text = marker.title
                details.text = marker.snippet
                return info
            }

            override fun getInfoWindow(marker: Marker): android.view.View? {
                return null // Use default frame
            }
        })

        // Load all markers from database
        dao.getAllLocationsLive().observe(this) { locations ->
            map.clear()
            locations.forEach { loc ->
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(LatLng(loc.latitude, loc.longitude))
                        .title(loc.address)
                        .snippet(
                            "ID: ${loc.id}\n" +
                                    "Lat: %.5f\nLng: %.5f".format(loc.latitude, loc.longitude)
                        )
                )
                marker?.tag = loc
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.showInfoWindow()
        return true
    }
}





