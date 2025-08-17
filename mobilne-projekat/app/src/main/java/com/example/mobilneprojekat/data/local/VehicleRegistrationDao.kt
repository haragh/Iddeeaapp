package com.example.mobilneprojekat.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleRegistrationDao {
    @Query("SELECT * FROM vehicle_registration_requests")
    fun getAll(): Flow<List<VehicleRegistrationRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(requests: List<VehicleRegistrationRequestEntity>)

    @Update
    suspend fun update(request: VehicleRegistrationRequestEntity)

    @Query("DELETE FROM vehicle_registration_requests")
    suspend fun clearAll()

    @Query("SELECT * FROM vehicle_registration_requests WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<VehicleRegistrationRequestEntity>>
} 