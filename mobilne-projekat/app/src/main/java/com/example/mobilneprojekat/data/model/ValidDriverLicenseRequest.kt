package com.example.mobilneprojekat.data.model

data class ValidDriverLicenseRequest(
    val entity: String?,
    val canton: String?,
    val municipality: String?,
    val institution: String?,
    val dateUpdate: String?,
    val maleTotal: Int?,
    val femaleTotal: Int?
) 