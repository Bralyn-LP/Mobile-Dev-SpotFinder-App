package com.example.spotfinder.data

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LocationEntity::class], version = 2, exportSchema = false)
abstract class SpotFinderDatabase : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: SpotFinderDatabase? = null

        fun getDatabase(context: Context): SpotFinderDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpotFinderDatabase::class.java,
                    "spotfinder_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SpotFinderDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun populateDatabase(dao: LocationDao) {
            CoroutineScope(Dispatchers.IO).launch {
                val initialLocations = listOf(
                    LocationEntity(address = "Oshawa", latitude = 43.8971, longitude = -78.8658),
                    LocationEntity(address = "Ajax", latitude = 43.8500, longitude = -79.0204),
                    LocationEntity(address = "Pickering", latitude = 43.8356, longitude = -79.0892),
                    LocationEntity(address = "Whitby", latitude = 43.8971, longitude = -78.8648),
                    LocationEntity(address = "Clarington", latitude = 43.9540, longitude = -78.6340),
                    LocationEntity(address = "Scarborough", latitude = 43.7725, longitude = -79.2570),
                    LocationEntity(address = "North York", latitude = 43.7615, longitude = -79.4111),
                    LocationEntity(address = "Etobicoke", latitude = 43.6275, longitude = -79.5204),
                    LocationEntity(address = "East York", latitude = 43.7061, longitude = -79.3285),
                    LocationEntity(address = "York", latitude = 43.7281, longitude = -79.4665),
                    LocationEntity(address = "Downtown Toronto", latitude = 43.65107, longitude = -79.347015),
                    LocationEntity(address = "Midtown Toronto", latitude = 43.7000, longitude = -79.4000),
                    LocationEntity(address = "Harbourfront", latitude = 43.6380, longitude = -79.3800),
                    LocationEntity(address = "Liberty Village", latitude = 43.6400, longitude = -79.4140),
                    LocationEntity(address = "Queen Street West", latitude = 43.6460, longitude = -79.4020),
                    LocationEntity(address = "King Street West", latitude = 43.6470, longitude = -79.3960),
                    LocationEntity(address = "St. Lawrence Market", latitude = 43.6490, longitude = -79.3730),
                    LocationEntity(address = "Distillery District", latitude = 43.6500, longitude = -79.3590),
                    LocationEntity(address = "Yorkville", latitude = 43.6700, longitude = -79.3860),
                    LocationEntity(address = "Bay Street Corridor", latitude = 43.6550, longitude = -79.3800),
                    LocationEntity(address = "Toronto Eaton Centre", latitude = 43.6540, longitude = -79.3800),
                    LocationEntity(address = "Financial District", latitude = 43.6480, longitude = -79.3800),
                    LocationEntity(address = "Waterfront Toronto", latitude = 43.6400, longitude = -79.3810),
                    LocationEntity(address = "Danforth Village", latitude = 43.6890, longitude = -79.3140),
                    LocationEntity(address = "Leaside Village", latitude = 43.7100, longitude = -79.3510),
                    LocationEntity(address = "Crescent Town", latitude = 43.7060, longitude = -79.3270),
                    LocationEntity(address = "Scarborough Town Centre", latitude = 43.7730, longitude = -79.2570),
                    LocationEntity(address = "Agincourt", latitude = 43.7810, longitude = -79.2560),
                    LocationEntity(address = "Guildwood", latitude = 43.7605, longitude = -79.2118),
                    LocationEntity(address = "Malvern", latitude = 43.8110, longitude = -79.1911),
                    LocationEntity(address = "Rouge", latitude = 43.8115, longitude = -79.1510),
                    LocationEntity(address = "Markham", latitude = 43.8561, longitude = -79.3370),
                    LocationEntity(address = "Vaughan", latitude = 43.8372, longitude = -79.5064),
                    LocationEntity(address = "Richmond Hill", latitude = 43.8828, longitude = -79.4403),
                    LocationEntity(address = "Newmarket", latitude = 44.0592, longitude = -79.4619),
                    LocationEntity(address = "Aurora", latitude = 44.0000, longitude = -79.4667),
                    LocationEntity(address = "Stouffville", latitude = 43.9530, longitude = -79.3030),
                    LocationEntity(address = "Brampton", latitude = 43.7315, longitude = -79.7624),
                    LocationEntity(address = "Mississauga", latitude = 43.5890, longitude = -79.6441),
                    LocationEntity(address = "Burlington", latitude = 43.3255, longitude = -79.7990),
                    LocationEntity(address = "Oakville", latitude = 43.4675, longitude = -79.6877),
                    LocationEntity(address = "Milton", latitude = 43.5181, longitude = -79.8774),
                    LocationEntity(address = "King City", latitude = 43.9464, longitude = -79.4798),
                    LocationEntity(address = "Unionville", latitude = 43.8680, longitude = -79.3370),
                    LocationEntity(address = "Thornhill", latitude = 43.7960, longitude = -79.4165),
                    LocationEntity(address = "Concord", latitude = 43.7844, longitude = -79.5161),
                    LocationEntity(address = "Woodbridge", latitude = 43.7833, longitude = -79.6167),
                    LocationEntity(address = "Georgetown", latitude = 43.6510, longitude = -79.9330),
                    LocationEntity(address = "Caledon", latitude = 43.8667, longitude = -79.9667),
                    LocationEntity(address = "Orangeville", latitude = 43.9167, longitude = -80.1000),
                    LocationEntity(address = "Uxbridge", latitude = 44.1167, longitude = -79.1333),
                    LocationEntity(address = "Acton", latitude = 43.6667, longitude = -80.0333),
                    LocationEntity(address = "Leaside", latitude = 43.7090, longitude = -79.3570),
                    LocationEntity(address = "Bayview Village", latitude = 43.7650, longitude = -79.3830),
                    LocationEntity(address = "Clairlea", latitude = 43.7240, longitude = -79.3170),
                    LocationEntity(address = "Islington", latitude = 43.6530, longitude = -79.5570),
                    LocationEntity(address = "Mimico", latitude = 43.6100, longitude = -79.5020),
                    LocationEntity(address = "Long Branch", latitude = 43.5900, longitude = -79.5320),
                    LocationEntity(address = "Sunnylea", latitude = 43.6550, longitude = -79.4960),
                    LocationEntity(address = "Kingsway", latitude = 43.6535, longitude = -79.4955),
                    LocationEntity(address = "Etobicoke North", latitude = 43.7130, longitude = -79.5580),
                    LocationEntity(address = "Etobicoke Centre", latitude = 43.6380, longitude = -79.5250),
                    LocationEntity(address = "Thorncliffe Park", latitude = 43.7090, longitude = -79.3380),
                    LocationEntity(address = "Rexdale", latitude = 43.7330, longitude = -79.5650),
                    LocationEntity(address = "Cabbagetown", latitude = 43.6667, longitude = -79.3667),
                    LocationEntity(address = "Riverdale", latitude = 43.6660, longitude = -79.3540),
                    LocationEntity(address = "Leslieville", latitude = 43.6700, longitude = -79.3520),
                    LocationEntity(address = "East Danforth", latitude = 43.6880, longitude = -79.3120),
                    LocationEntity(address = "Weston-Pelham", latitude = 43.7000, longitude = -79.5200),
                    LocationEntity(address = "York University Area", latitude = 43.7730, longitude = -79.5010),
                    LocationEntity(address = "Eglinton West", latitude = 43.7090, longitude = -79.4350),
                    LocationEntity(address = "Finch West", latitude = 43.7590, longitude = -79.5160),
                    LocationEntity(address = "Downsview", latitude = 43.7250, longitude = -79.4870),
                    LocationEntity(address = "Toronto Islands", latitude = 43.6167, longitude = -79.3833),
                    LocationEntity(address = "York Mills", latitude = 43.7520, longitude = -79.3620),
                    LocationEntity(address = "Don Mills", latitude = 43.7292, longitude = -79.3361),
                    LocationEntity(address = "Jane and Finch", latitude = 43.7550, longitude = -79.5250),
                    LocationEntity(address = "Port Union", latitude = 43.7844, longitude = -79.2122),
                    LocationEntity(address = "Guildwood Heights", latitude = 43.7570, longitude = -79.2160),
                    LocationEntity(address = "Etobicoke Lakeshore", latitude = 43.6080, longitude = -79.5320),
                    LocationEntity(address = "Church-Wellesley Village", latitude = 43.6670, longitude = -79.3770),
                    LocationEntity(address = "Chinatown", latitude = 43.6536, longitude = -79.3959),
                    LocationEntity(address = "Regent Park", latitude = 43.6575, longitude = -79.3643),
                    LocationEntity(address = "Harbord Village", latitude = 43.6610, longitude = -79.4090),
                    LocationEntity(address = "Palmerston-Little Italy", latitude = 43.6550, longitude = -79.4170),
                    LocationEntity(address = "Cabbagetown-South St. James Town", latitude = 43.6667, longitude = -79.3667),
                    LocationEntity(address = "Forest Hill", latitude = 43.7000, longitude = -79.4300),
                    LocationEntity(address = "The Annex", latitude = 43.6680, longitude = -79.4030),
                    LocationEntity(address = "Kingston Road Area", latitude = 43.7180, longitude = -79.3010),
                    LocationEntity(address = "Scarborough Bluffs", latitude = 43.6990, longitude = -79.2290),
                    LocationEntity(address = "Toronto Zoo Area", latitude = 43.8170, longitude = -79.1850),
                    LocationEntity(address = "High Park", latitude = 43.6466, longitude = -79.4637),
                    LocationEntity(address = "Etobicoke Humber Valley", latitude = 43.6460, longitude = -79.5400),
                    LocationEntity(address = "Bluffers Park", latitude = 43.7000, longitude = -79.2200),
                    LocationEntity(address = "Cherry Beach", latitude = 43.6370, longitude = -79.3550),
                    LocationEntity(address = "Tommy Thompson Park", latitude = 43.6425, longitude = -79.3570),
                    LocationEntity(address = "Sunnyside Beach", latitude = 43.6280, longitude = -79.4670),
                    LocationEntity(address = "Leslie Street Spit", latitude = 43.6350, longitude = -79.3500),
                    LocationEntity(address = "Guild Park and Gardens", latitude = 43.7550, longitude = -79.2040),
                    LocationEntity(address = "Crothers Woods", latitude = 43.7120, longitude = -79.3420),
                    LocationEntity(address = "Tom Longboat Park", latitude = 43.6470, longitude = -79.3620)
                )

                initialLocations.forEach { dao.insertLocation(it) }
                Log.d("DB_POPULATE", "Inserted ${initialLocations.size} locations")
            }
        }
    }

    private class SpotFinderDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // Safely call populateDatabase from companion
            INSTANCE?.let { database ->
                populateDatabase(database.locationDao())
            }
        }
    }
}
