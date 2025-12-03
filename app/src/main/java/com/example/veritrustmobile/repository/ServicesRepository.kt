package com.example.veritrustmobile.repository

import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.model.Servicio

class ServicesRepository {
    private val api = RetrofitClient.api

    suspend fun getServicios(): List<Servicio> {
        val response = api.getServicios()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error API ${response.code()}: No se pudieron obtener los servicios.")
        }
    }

    suspend fun crearServicio(servicio: Servicio): Servicio {
        val response = api.crearServicio(servicio)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Error API ${response.code()}: No se pudo crear el servicio.")
        }
    }

    suspend fun actualizarServicio(id: String, servicio: Servicio): Servicio {
        val response = api.actualizarServicio(id, servicio)
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception("Error API ${response.code()}: No se pudo actualizar el servicio.")
        }
    }

    suspend fun eliminarServicio(id: String) {
        val response = api.eliminarServicio(id)
        if (!response.isSuccessful) {
            throw Exception("Error API ${response.code()}: No se pudo eliminar el servicio.")
        }
    }
}
