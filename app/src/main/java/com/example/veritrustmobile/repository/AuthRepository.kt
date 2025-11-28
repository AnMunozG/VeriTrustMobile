package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.User
import com.example.veritrustmobile.data.RetrofitClient

class AuthRepository {

    suspend fun login(email: String, pass: String): User? {
        return try {
            val userRequest = User(user = email, password = pass)

            val response = RetrofitClient.api.login(userRequest)

            if (response.isSuccessful) {
                response.body()
            } else {
                println("ERROR LOGIN CÓDIGO: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun registrarUsuario(
        rut: String, nombre: String, fechaNacimiento: String,
        telefono: String, email: String, contrasena: String
    ): Boolean {
        return try {
            val nuevoUsuario = User(
                rut = rut,
                nombre = nombre,
                fechaNacimiento = fechaNacimiento,
                telefono = telefono,
                user = email,
                password = contrasena
            )
            val response = RetrofitClient.api.registrar(nuevoUsuario)

            if (!response.isSuccessful) {
                println("ERROR REGISTRO: ${response.code()} - ${response.errorBody()?.string()}")
            }

            response.isSuccessful

        } catch (e: Exception) {
            println("ERROR CONEXIÓN: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}