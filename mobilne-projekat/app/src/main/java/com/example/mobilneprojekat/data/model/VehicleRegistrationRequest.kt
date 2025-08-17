package com.example.mobilneprojekat.data.model

data class VehicleRegistrationRequest(
    val entity: String?,
    val canton: String?,
    val municipality: String?,
    val year: Int?,
    val month: Int?,
    val dateUpdate: String?,
    val registrationPlace: String?,
    val permanentRegistration: Int?,
    val firstTimeRequestsTotal: Int?,
    val renewalRequestsTotal: Int?,
    val ownershipChangesTotal: Int?,
    val deregisteredTotal: Int?
) 