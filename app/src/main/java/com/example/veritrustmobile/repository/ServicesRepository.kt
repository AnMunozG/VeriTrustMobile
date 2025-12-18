package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.data.local.ServicioDao
import kotlinx.coroutines.flow.firstOrNull

class ServicesRepository(private val servicioDao: ServicioDao?) { // ⭐ Hacer el Dao opcional

    suspend fun getAllServices(): List<Servicio> {
        return try {
            val response = RetrofitClient.api.getServicios()

            if (response.isSuccessful) {
                val services = response.body() ?: emptyList()
                // Guardar en la base de datos local solo si el Dao existe
                if (services.isNotEmpty() && servicioDao != null) {
                    servicioDao.deleteAll()
                    servicioDao.insertServicios(services)
                }
                services
            } else {
                // Si la API falla, intentar cargar de la DB local si el Dao existe
                servicioDao?.getAllServicios()?.firstOrNull() ?: emptyList()
            }
        } catch (e: Exception) {
            // Si no hay conexión, cargar de la DB local si el Dao existe
            println("Error de conexión, cargando desde cache: ${e.message}")
            servicioDao?.getAllServicios()?.firstOrNull() ?: emptyList()
        }
    }
    // ... resto del archivo


    suspend fun getServicios(): List<Servicio> {
        return getAllServices()
    }

    suspend fun crearServicio(servicio: Servicio) {
        val response = RetrofitClient.api.crearServicio(servicio)
        if (!response.isSuccessful) {
            throw Exception("Error al crear servicio: ${response.code()}")
        }
    }

    suspend fun actualizarServicio(id: String, servicio: Servicio) {
        val response = RetrofitClient.api.actualizarServicio(id, servicio)
        if (!response.isSuccessful) {
            throw Exception("Error al actualizar servicio: ${response.code()}")
        }
    }

    suspend fun eliminarServicio(id: String) {
        val response = RetrofitClient.api.eliminarServicio(id)
        if (!response.isSuccessful) {
            throw Exception("Error al eliminar servicio: ${response.code()}")
        }
    }
}
