package com.example.mobilneprojekat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mobilneprojekat.data.local.ValidDriverLicenseRequestEntity
import com.example.mobilneprojekat.data.local.ValidDriverLicenseDao

@Database(entities = [VehicleRegistrationRequestEntity::class, ValidDriverLicenseRequestEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleRegistrationDao(): VehicleRegistrationDao
    abstract fun validDriverLicenseDao(): ValidDriverLicenseDao
} 