package com.example.mobilneprojekat.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle_registration_requests")
data class VehicleRegistrationRequestEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entity: String = "",
    val canton: String = "",
    val municipality: String = "",
    val year: Int = 0,
    val month: Int = 0,
    val dateUpdate: String = "",
    val registrationPlace: String = "",
    val permanentRegistration: Int = 0,
    val firstTimeRequestsTotal: Int = 0,
    val renewalRequestsTotal: Int = 0,
    val ownershipChangesTotal: Int = 0,
    val deregisteredTotal: Int = 0,
    val isFavorite: Boolean = false
) 