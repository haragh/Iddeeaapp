package com.example.mobilneprojekat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilneprojekat.data.local.VehicleRegistrationRequestEntity
import com.example.mobilneprojekat.data.repository.VehicleRegistrationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle

class VehicleRegistrationViewModel(
    private val repository: VehicleRegistrationRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object {
        private const val KEY_FILTER = "filter"
        private const val KEY_SORT_BY = "sortBy"
    }
    private val _filter = MutableStateFlow(savedStateHandle.get<String>(KEY_FILTER) ?: "")
    private val _sortBy = MutableStateFlow(savedStateHandle.get<String>(KEY_SORT_BY) ?: "year")
    private val _registrations = repository.getAll()
        .combine(_filter) { list, filter ->
            if (filter.isBlank()) list else list.filter {
                it.municipality.contains(filter, ignoreCase = true) ||
                it.canton.contains(filter, ignoreCase = true)
            }
        }
        .combine(_sortBy) { list, sortBy ->
            when (sortBy) {
                "year" -> list.sortedByDescending { it.year }
                "month" -> list.sortedByDescending { it.month }
                else -> list
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val registrations: StateFlow<List<VehicleRegistrationRequestEntity>> = _registrations
    val filter: StateFlow<String> = _filter
    val sortBy: StateFlow<String> = _sortBy

    fun setFilter(value: String) {
        _filter.value = value
        savedStateHandle[KEY_FILTER] = value
    }
    fun setSortBy(value: String) {
        _sortBy.value = value
        savedStateHandle[KEY_SORT_BY] = value
    }

    fun refresh(limit: Int = 100) {
        viewModelScope.launch {
            try {
                repository.refresh(limit)
                _error.value = null
            } catch (e: Exception) {
                android.util.Log.e("VehicleVM", "Error in refresh: ", e)
                _error.value = e.localizedMessage ?: "Došlo je do greške pri dohvaćanju podataka."
            }
        }
    }

    fun setFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch { repository.setFavorite(id, isFavorite) }
    }

    fun clearError() { _error.value = null }
}

class VehicleRegistrationViewModelFactory(
    private val context: android.content.Context,
    private val token: String,
    private val savedStateHandle: SavedStateHandle? = null
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleRegistrationViewModel::class.java)) {
            val repository = com.example.mobilneprojekat.utils.DI.provideRepository(context, token)
            @Suppress("UNCHECKED_CAST")
            return VehicleRegistrationViewModel(repository, savedStateHandle ?: SavedStateHandle()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 