package com.example.veritrustmobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.repository.ServicesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ServiciosViewModel(application: Application) : AndroidViewModel(application) {

    private val servicesRepository = ServicesRepository(application)

    private val _servicesState = MutableStateFlow<List<Servicio>>(emptyList())
    val servicesState = _servicesState.asStateFlow()

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            val servicesFromDb = withContext(Dispatchers.IO) {
                servicesRepository.getAllServices()
            }
            _servicesState.value = servicesFromDb
        }
    }
}
