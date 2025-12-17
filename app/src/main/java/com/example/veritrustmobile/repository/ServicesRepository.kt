package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.data.RetrofitClient

class ServicesRepository {

    suspend fun getAllServices(): List<Servicio> {
        return try {
            val response = RetrofitClient.api.getServicios()

            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                println("Error API: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            println("Error de conexión: ${e.message}")
            emptyList()
        }
    }

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