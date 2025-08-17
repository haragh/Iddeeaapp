package com.example.mobilneprojekat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "valid_driver_license_requests")
data class ValidDriverLicenseRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entity: String = "",
    val canton: String = "",
    val municipality: String = "",
    val institution: String = "",
    val dateUpdate: String = "",
    val maleTotal: Int = 0,
    val femaleTotal: Int = 0,
    val isFavorite: Boolean = false
) 