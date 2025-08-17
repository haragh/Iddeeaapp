package com.example.mobilneprojekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilneprojekat.data.local.ValidDriverLicenseRequestEntity
import com.example.mobilneprojekat.data.repository.ValidDriverLicenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider

class ValidDriverLicenseViewModel(
    private val repository: ValidDriverLicenseRepository
) : ViewModel() {
    private val _filter = MutableStateFlow("")
    private val _sortBy = MutableStateFlow("maleTotal")
    private val _licenses = repository.getAll()
        .combine(_filter) { list, filter ->
            if (filter.isBlank()) list else list.filter {
                it.municipality.contains(filter, ignoreCase = true) ||
                it.canton.contains(filter, ignoreCase = true)
            }
        }
        .combine(_sortBy) { list, sortBy ->
            when (sortBy) {
                "maleTotal" -> list.sortedByDescending { it.maleTotal }
                "femaleTotal" -> list.sortedByDescending { it.femaleTotal }
                else -> list
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val licenses: StateFlow<List<ValidDriverLicenseRequestEntity>> = _licenses
    val filter: StateFlow<String> = _filter
    val sortBy: StateFlow<String> = _sortBy

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun setFilter(value: String) { _filter.value = value }
    fun setSortBy(value: String) { _sortBy.value = value }

    fun refresh(limit: Int = 20) {
        viewModelScope.launch {
            try {
                repository.refresh(limit)
                _error.value = null
            } catch (e: Exception) {
                android.util.Log.e("ValidDLVM", "Error in refresh: ", e)
                _error.value = e.localizedMessage ?: "Došlo je do greške pri dohvaćanju podataka."
            }
        }
    }

    fun setFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch { repository.setFavorite(id, isFavorite) }
    }

    fun clearError() { _error.value = null }
}

class ValidDriverLicenseViewModelFactory(
    private val repository: ValidDriverLicenseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ValidDriverLicenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ValidDriverLicenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 