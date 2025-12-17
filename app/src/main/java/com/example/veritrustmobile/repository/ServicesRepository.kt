package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.data.local.ServicioDao
import kotlinx.coroutines.flow.Flow

class ServicesRepository(private val servicioDao: ServicioDao) {

    val allServices: Flow<List<Servicio>> = servicioDao.getAllServicios()

    suspend fun refreshServices() {
        try {
            val response = RetrofitClient.api.getServicios()
            if (response.isSuccessful) {
                val services = response.body() ?: emptyList()
                if (services.isNotEmpty()) {
                    servicioDao.deleteAll()
                    servicioDao.insertServicios(services)
                }
            } else {
                println("Error API: ${response.code()}")
            }
        } catch (e: Exception) {
            println("Error de conexión: ${e.message}")
            // Si falla la API, no hacemos nada, ya que el ViewModel observa Room
        }
    }
}