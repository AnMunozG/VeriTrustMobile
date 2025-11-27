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
}