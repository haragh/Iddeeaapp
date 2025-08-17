package com.example.mobilneprojekat.data.repository

import com.example.mobilneprojekat.data.local.VehicleRegistrationDao
import com.example.mobilneprojekat.data.local.VehicleRegistrationRequestEntity
import com.example.mobilneprojekat.data.model.VehicleRegistrationRequest
import com.example.mobilneprojekat.data.remote.VehicleRegistrationApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import android.util.Log

class VehicleRegistrationRepository(
    private val api: VehicleRegistrationApi,
    private val dao: VehicleRegistrationDao
) {
    fun getAll(): Flow<List<VehicleRegistrationRequestEntity>> = dao.getAll()
    fun getFavorites(): Flow<List<VehicleRegistrationRequestEntity>> = dao.getFavorites()

    suspend fun refresh(limit: Int = 20) {
        try {
            val oldFavorites = dao.getAll().first().filter { it.isFavorite }
            val response = api.getVehicleRegistrations(emptyMap(), limit)
            val remote = response.result ?: emptyList()
            android.util.Log.d("VehicleRepo", "API response size: ${remote.size}")
            if (remote.isNotEmpty()) android.util.Log.d("VehicleRepo", "First item: ${remote[0]}")
            val entities = remote.map { it.toEntity() }
            dao.clearAll()
            dao.insertAll(entities)
            val all = dao.getAll().first()
            oldFavorites.forEach { fav ->
                val match = all.find {
                    it.entity == fav.entity &&
                    it.canton == fav.canton &&
                    it.municipality == fav.municipality &&
                    it.year == fav.year &&
                    it.month == fav.month &&
                    it.dateUpdate == fav.dateUpdate &&
                    it.registrationPlace == fav.registrationPlace
                }
                if (match != null) {
                    dao.update(match.copy(isFavorite = true))
                }
            }
        } catch (e: Exception) {
            Log.e("VehicleRepo", "Error refreshing data: ", e)
        }
    }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        try {
            val current = dao.getAll().first().find { it.id == id } ?: return
            dao.update(current.copy(isFavorite = isFavorite))
        } catch (e: Exception) {
            Log.e("VehicleRepo", "Error setting favorite: ", e)
        }
    }
}

fun VehicleRegistrationRequest.toEntity() = VehicleRegistrationRequestEntity(
    entity = entity ?: "",
    canton = canton ?: "",
    municipality = municipality ?: "",
    year = year ?: 0,
    month = month ?: 0,
    dateUpdate = dateUpdate ?: "",
    registrationPlace = registrationPlace ?: "",
    permanentRegistration = permanentRegistration ?: 0,
    firstTimeRequestsTotal = firstTimeRequestsTotal ?: 0,
    renewalRequestsTotal = renewalRequestsTotal ?: 0,
    ownershipChangesTotal = ownershipChangesTotal ?: 0,
    deregisteredTotal = deregisteredTotal ?: 0
) 