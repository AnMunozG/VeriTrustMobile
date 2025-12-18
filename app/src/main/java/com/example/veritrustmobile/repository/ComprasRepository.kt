package com.example.veritrustmobile.repository

import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.model.Compra

class ComprasRepository {

    suspend fun getAllCompras(): List<Compra> {
        return try {
            val response = RetrofitClient.api.getAllCompras()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                println("Error API: ${response.code()}")
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun crearCompra(compra: Compra): Boolean {
        return try {
            val response = RetrofitClient.api.crearCompra(compra)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getComprasByUsuario(usuarioId: Long): List<Compra> {
        return try {
            val response = RetrofitClient.api.getComprasByUsuario(usuarioId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}