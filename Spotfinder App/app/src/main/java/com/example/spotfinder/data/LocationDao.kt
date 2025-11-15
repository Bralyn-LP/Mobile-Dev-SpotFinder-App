package com.example.spotfinder.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface LocationDao {

    // Query for a specific address
    @Query("SELECT * FROM locations WHERE address LIKE :address LIMIT 1")
    suspend fun getLocationByAddress(address: String): LocationEntity?

    // Add new location
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    // Update existing location
    @Update
    suspend fun updateLocation(location: LocationEntity)

    // Delete a location
    @Delete
    suspend fun deleteLocation(location: LocationEntity)

    // Get all locations (suspend for manual use)
    @Query("SELECT * FROM locations")
    suspend fun getAllLocations(): List<LocationEntity>

    // Get all locations as LiveData for dynamic map updates
    @Query("SELECT * FROM locations ORDER BY id ASC")
    fun getAllLocationsLive(): LiveData<List<LocationEntity>>

    // Get location via ID
    @Query("SELECT * FROM locations WHERE id = :id")
    suspend fun getLocationById(id: Int): LocationEntity?
}

