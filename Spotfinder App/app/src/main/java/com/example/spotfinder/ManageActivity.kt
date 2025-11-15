package com.example.spotfinder

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spotfinder.data.LocationEntity
import com.example.spotfinder.data.SpotFinderDatabase
import kotlinx.coroutines.launch

class ManageActivity : AppCompatActivity() {

    private lateinit var inputId: EditText
    private lateinit var inputAddress: EditText
    private lateinit var inputLatitude: EditText
    private lateinit var inputLongitude: EditText

    private lateinit var addButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var backButton: Button

    private val dao by lazy { SpotFinderDatabase.getDatabase(this).locationDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_locations)

        inputId = findViewById(R.id.inputId)
        inputAddress = findViewById(R.id.inputAddress)
        inputLatitude = findViewById(R.id.inputLatitude)
        inputLongitude = findViewById(R.id.inputLongitude)

        addButton = findViewById(R.id.addButton)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)
        backButton = findViewById(R.id.backButton)

        addButton.setOnClickListener { addLocation() }
        updateButton.setOnClickListener { updateLocation() }
        deleteButton.setOnClickListener { deleteLocation() }
        backButton.setOnClickListener { finish() }
    }

    private fun addLocation() {
        val address = inputAddress.text.toString().trim()
        val lat = inputLatitude.text.toString().toDoubleOrNull()
        val lon = inputLongitude.text.toString().toDoubleOrNull()

        if (address.isEmpty() || lat == null || lon == null ||
            lat !in -90.0..90.0 || lon !in -180.0..180.0
        ) {
            Toast.makeText(this, "Enter valid address and coordinates (-90≤lat≤90, -180≤lon≤180)", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            dao.insertLocation(LocationEntity(address = address, latitude = lat, longitude = lon))
            runOnUiThread {
                Toast.makeText(this@ManageActivity, "Added: $address ($lat, $lon)", Toast.LENGTH_SHORT).show()
                clearInputs()
            }
        }
    }

    private fun updateLocation() {
        val id = inputId.text.toString().toIntOrNull()
        val address = inputAddress.text.toString().trim()
        val lat = inputLatitude.text.toString().toDoubleOrNull()
        val lon = inputLongitude.text.toString().toDoubleOrNull()

        if (id == null || address.isEmpty() || lat == null || lon == null ||
            lat !in -90.0..90.0 || lon !in -180.0..180.0
        ) {
            Toast.makeText(this, "Enter valid ID and coordinates", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val existing = dao.getLocationById(id)
            if (existing != null) {
                dao.updateLocation(existing.copy(address = address, latitude = lat, longitude = lon))
                runOnUiThread {
                    Toast.makeText(this@ManageActivity, "Updated ID $id", Toast.LENGTH_SHORT).show()
                    clearInputs()
                }
            } else {
                runOnUiThread { Toast.makeText(this@ManageActivity, "Location ID not found", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun deleteLocation() {
        val id = inputId.text.toString().toIntOrNull()
        val address = inputAddress.text.toString().trim().takeIf { it.isNotEmpty() }

        if (id == null && address == null) {
            Toast.makeText(this, "Enter either ID or Address to delete", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            var location: LocationEntity? = null
            if (id != null) location = dao.getLocationById(id)
            if (location == null && address != null) location = dao.getLocationByAddress(address)

            if (location != null) {
                dao.deleteLocation(location)
                runOnUiThread {
                    Toast.makeText(this@ManageActivity, "Deleted: ${location.address} (ID ${location.id})", Toast.LENGTH_SHORT).show()
                    clearInputs()
                }
            } else {
                runOnUiThread { Toast.makeText(this@ManageActivity, "Location not found", Toast.LENGTH_SHORT).show() }
            }
        }
    }

    private fun clearInputs() {
        inputId.text.clear()
        inputAddress.text.clear()
        inputLatitude.text.clear()
        inputLongitude.text.clear()
    }
}





