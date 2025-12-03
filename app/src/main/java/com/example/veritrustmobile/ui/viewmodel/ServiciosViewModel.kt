package com.example.veritrustmobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.repository.ServicesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class ServiciosViewModel : ViewModel() {

    private val servicesRepository = ServicesRepository()

    private val _servicesState = MutableStateFlow<UiState<List<Servicio>>>(UiState.Loading)
    val servicesState = _servicesState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = UiState.Loading
            try {
                val servicesFromApi = servicesRepository.getServicios()
                _servicesState.value = UiState.Success(servicesFromApi)
            } catch (e: Exception) {
                _servicesState.value = UiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
