package com.example.mobilneprojekat.data.repository

import com.example.mobilneprojekat.data.local.ValidDriverLicenseDao
import com.example.mobilneprojekat.data.local.ValidDriverLicenseRequestEntity
import com.example.mobilneprojekat.data.model.ValidDriverLicenseRequest
import com.example.mobilneprojekat.data.remote.ValidDriverLicenseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import android.util.Log

class ValidDriverLicenseRepository(
    private val api: ValidDriverLicenseApi,
    private val dao: ValidDriverLicenseDao
) {
    fun getAll(): Flow<List<ValidDriverLicenseRequestEntity>> = dao.getAll()
    fun getFavorites(): Flow<List<ValidDriverLicenseRequestEntity>> = dao.getFavorites()

    suspend fun refresh(limit: Int = 20) {
        try {
            val oldFavorites = dao.getAll().first().filter { it.isFavorite }
            val response = api.getValidDriverLicenses(emptyMap(), limit)
            val remote = response.result ?: emptyList()
            val entities = remote.map { it.toEntity() }
            dao.clearAll()
            dao.insertAll(entities)
            val all = dao.getAll().first()
            oldFavorites.forEach { fav ->
                val match = all.find {
                    it.entity == fav.entity &&
                    it.canton == fav.canton &&
                    it.municipality == fav.municipality &&
                    it.institution == fav.institution &&
                    it.dateUpdate == fav.dateUpdate
                }
                if (match != null) {
                    dao.update(match.copy(isFavorite = true))
                }
            }
        } catch (e: Exception) {
            Log.e("ValidDLRepo", "Error refreshing data: ", e)
        }
    }

    suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        try {
            val current = dao.getAll().first().find { it.id == id } ?: return
            dao.update(current.copy(isFavorite = isFavorite))
        } catch (e: Exception) {
            Log.e("ValidDLRepo", "Error setting favorite: ", e)
        }
    }
}

fun ValidDriverLicenseRequest.toEntity() = ValidDriverLicenseRequestEntity(
    entity = entity ?: "",
    canton = canton ?: "",
    municipality = municipality ?: "",
    institution = institution ?: "",
    dateUpdate = dateUpdate ?: "",
    maleTotal = maleTotal ?: 0,
    femaleTotal = femaleTotal ?: 0
) 