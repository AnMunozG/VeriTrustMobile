package com.example.veritrustmobile.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.data.local.AppDatabase
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.repository.ServicesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServicesRepository

    val servicesState: StateFlow<List<Servicio>>

    init {
        val database = AppDatabase.getDatabase(application)
        val servicioDao = database.servicioDao()
        repository = ServicesRepository(servicioDao)

        servicesState = repository.allServices.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        refreshServices()
    }

    fun refreshServices() {
        viewModelScope.launch {
            repository.refreshServices()
        }
    }
}