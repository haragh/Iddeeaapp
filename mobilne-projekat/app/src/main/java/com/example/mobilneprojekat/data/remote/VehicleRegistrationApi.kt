package com.example.mobilneprojekat.data.remote

import com.example.mobilneprojekat.data.model.VehicleRegistrationRequest
import com.example.mobilneprojekat.data.model.VehicleRegistrationResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.http.POST

interface VehicleRegistrationApi {
    @POST("api/VehicleRegistrationRequests/list")
    suspend fun getVehicleRegistrations(
        @Body body: Map<String, @JvmSuppressWildcards Any> = emptyMap(),
        @Query("limit") limit: Int = 20
    ): VehicleRegistrationResponse
} 