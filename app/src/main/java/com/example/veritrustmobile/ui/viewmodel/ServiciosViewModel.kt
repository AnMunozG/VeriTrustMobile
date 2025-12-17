package com.example.veritrustmobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.data.local.AppDatabase
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.repository.ServicesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Servicio>) : UiState()
    data class Error(val message: String) : UiState()
}

class ServiciosViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val servicesRepository = ServicesRepository(database.servicioDao())

    private val _servicesState = MutableStateFlow<UiState>(UiState.Loading)
    val servicesState = _servicesState.asStateFlow()

    init {
        loadServices()
    }

    fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = UiState.Loading
            try {
                val servicesFromApi = servicesRepository.getAllServices()
                _servicesState.value = UiState.Success(servicesFromApi)
            } catch (e: Exception) {
                _servicesState.value = UiState.Error(e.message ?: "Error al cargar servicios")
            }
        }
    }
}
