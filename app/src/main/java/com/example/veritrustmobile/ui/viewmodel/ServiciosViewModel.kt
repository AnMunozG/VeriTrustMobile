package com.example.veritrustmobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.repository.ServicesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiciosViewModel : ViewModel() {

    private val servicesRepository = ServicesRepository()

    private val _servicesState = MutableStateFlow<List<Servicio>>(emptyList())
    val servicesState = _servicesState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            val servicesFromApi = servicesRepository.getAllServices()
            _servicesState.value = servicesFromApi
        }
    }
}