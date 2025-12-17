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

sealed class BackOfficeUiState {
    object Loading : BackOfficeUiState()
    data class Success(val servicios: List<Servicio>) : BackOfficeUiState()
    data class Error(val message: String) : BackOfficeUiState()
}

class BackOfficeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val servicioRepository = ServicesRepository(database.servicioDao())

    private val _uiState = MutableStateFlow<BackOfficeUiState>(BackOfficeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        cargarServicios()
    }

    fun cargarServicios() {
        viewModelScope.launch {
            _uiState.value = BackOfficeUiState.Loading
            try {
                val servicios = servicioRepository.getServicios()
                _uiState.value = BackOfficeUiState.Success(servicios)
            } catch (e: Exception) {
                _uiState.value = BackOfficeUiState.Error(e.message ?: "Error desconocido al cargar servicios.")
            }
        }
    }
    fun eliminarServicio(id: String) {
        viewModelScope.launch {
            try {
                servicioRepository.eliminarServicio(id)
                cargarServicios()
            } catch (e: Exception) {
                _uiState.value = BackOfficeUiState.Error(e.message ?: "Error al eliminar el servicio.")
            }
        }
    }

    fun crearServicio(servicio: Servicio) {
        viewModelScope.launch {
            try {
                servicioRepository.crearServicio(servicio)
                cargarServicios()
            } catch (e: Exception) {
                _uiState.value = BackOfficeUiState.Error(e.message ?: "Error al crear el servicio.")
            }
        }
    }


    fun actualizarServicio(id: String, servicio: Servicio) {
        viewModelScope.launch {
            try {
                servicioRepository.actualizarServicio(id, servicio)
                cargarServicios()
            } catch (e: Exception) {
                _uiState.value = BackOfficeUiState.Error(e.message ?: "Error al actualizar el servicio.")
            }
        }
    }
}
