package com.example.mobilneprojekat.utils

import android.content.Context
import androidx.room.Room
import com.example.mobilneprojekat.data.local.AppDatabase
import com.example.mobilneprojekat.data.remote.VehicleRegistrationApi
import com.example.mobilneprojekat.data.remote.ValidDriverLicenseApi
import com.example.mobilneprojekat.data.repository.VehicleRegistrationRepository
import com.example.mobilneprojekat.data.repository.ValidDriverLicenseRepository
import com.example.mobilneprojekat.viewmodel.VehicleRegistrationViewModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DI {
    fun provideDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()

    fun provideApi(token: String): VehicleRegistrationApi {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()
        return Retrofit.Builder()
        .baseUrl("https://odp.iddeea.gov.ba:8096/")
            .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VehicleRegistrationApi::class.java)
    }

    fun provideRepository(context: Context, token: String): VehicleRegistrationRepository {
        val db = provideDatabase(context)
        val api = provideApi(token)
        return VehicleRegistrationRepository(api, db.vehicleRegistrationDao())
    }

    fun provideValidDriverLicenseApi(token: String): ValidDriverLicenseApi {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed(request)
            }
            .build()
        return Retrofit.Builder()
            .baseUrl("https://odp.iddeea.gov.ba:8096/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ValidDriverLicenseApi::class.java)
    }

    fun provideValidDriverLicenseRepository(context: Context, token: String): ValidDriverLicenseRepository {
        val db = provideDatabase(context)
        val api = provideValidDriverLicenseApi(token)
        return ValidDriverLicenseRepository(api, db.validDriverLicenseDao())
    }
} 