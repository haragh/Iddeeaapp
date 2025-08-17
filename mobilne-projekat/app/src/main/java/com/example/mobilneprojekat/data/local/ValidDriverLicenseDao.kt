package com.example.mobilneprojekat.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ValidDriverLicenseDao {
    @Query("SELECT * FROM valid_driver_license_requests")
    fun getAll(): Flow<List<ValidDriverLicenseRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requests: List<ValidDriverLicenseRequestEntity>)

    @Update
    suspend fun update(request: ValidDriverLicenseRequestEntity)

    @Query("DELETE FROM valid_driver_license_requests")
    suspend fun clearAll()

    @Query("SELECT * FROM valid_driver_license_requests WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<ValidDriverLicenseRequestEntity>>
} 