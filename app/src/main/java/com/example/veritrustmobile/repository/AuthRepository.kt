package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.User
import com.example.veritrustmobile.data.RetrofitClient

class AuthRepository {

    // Función de Login (Sin cambios, pero necesaria para que el archivo esté completo)
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

    // Función de Registro ACTUALIZADA (Recibe los nuevos campos)
    suspend fun registrarUsuario(
        rut: String,
        nombre: String,
        fechaNacimiento: String,
        telefono: String,
        email: String,
        contrasena: String,
        region: String,  // <--- Nuevo parámetro obligatorio
        genero: String   // <--- Nuevo parámetro obligatorio
    ): Boolean {
        return try {
            // Creamos el objeto User con TODOS los datos para enviar al Backend
            val nuevoUsuario = User(
                user = email,           // Mapeamos email -> user
                password = contrasena,  // Mapeamos contrasena -> password
                rut = rut,
                nombre = nombre,
                fechaNacimiento = fechaNacimiento,
                telefono = telefono,
                region = region,        // <--- Enviamos la región
                genero = genero         // <--- Enviamos el género
            )

            // Llamada a la API
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