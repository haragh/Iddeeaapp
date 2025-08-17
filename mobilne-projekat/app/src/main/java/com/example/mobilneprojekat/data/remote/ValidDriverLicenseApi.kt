package com.example.mobilneprojekat.data.remote

import com.example.mobilneprojekat.data.model.ValidDriverLicenseResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ValidDriverLicenseApi {
    @POST("api/ValidDriverLicenses/list")
    suspend fun getValidDriverLicenses(
        @Body body: Map<String, @JvmSuppressWildcards Any> = emptyMap(),
        @Query("limit") limit: Int = 20
    ): ValidDriverLicenseResponse
} 