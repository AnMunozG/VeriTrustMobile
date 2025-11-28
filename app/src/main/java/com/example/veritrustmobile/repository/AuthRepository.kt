package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.User
import com.example.veritrustmobile.data.RetrofitClient

class AuthRepository {

    /**
     * LOGIN: Envía credenciales a la API
     */
    suspend fun login(email: String, pass: String): User? {
        return try {
            val userRequest = User(user = email, password = pass)

            val response = RetrofitClient.api.login(userRequest)

            if (response.isSuccessful) {
                response.body()
            } else {
                // También es útil ver por qué falla el login
                println("🚨 ERROR LOGIN CÓDIGO: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * REGISTRO: Envía todos los datos del formulario
     * Incluye logs detallados para detectar errores 400, 409, 500.
     */
    suspend fun registrarUsuario(
        rut: String, nombre: String, fechaNacimiento: String,
        telefono: String, email: String, contrasena: String
    ): Boolean {
        return try {
            // 1. Crear el objeto con todos los datos
            val nuevoUsuario = User(
                rut = rut,
                nombre = nombre,
                fechaNacimiento = fechaNacimiento,
                telefono = telefono,
                user = email,
                password = contrasena
            )

            // 2. LLAMADA REAL A TU BACKEND
            val response = RetrofitClient.api.registrar(nuevoUsuario)

            // Logs para depurar si algo falla
            if (!response.isSuccessful) {
                println("❌ ERROR REGISTRO: ${response.code()} - ${response.errorBody()?.string()}")
            }

            response.isSuccessful

        } catch (e: Exception) {
            println("❌ ERROR CONEXIÓN: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}