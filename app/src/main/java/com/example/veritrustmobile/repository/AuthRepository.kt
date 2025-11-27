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
        rut: String,
        nombre: String,
        fechaNacimiento: String,
        telefono: String,
        email: String,
        contrasena: String
    ): Boolean {
        return try {
            // 1. Empaquetamos los datos en el objeto User
            // Asegúrate de que los nombres de parámetros coincidan con tu modelo User actualizado
            val nuevoUsuario = User(
                rut = rut,
                nombre = nombre,
                fechaNacimiento = fechaNacimiento,
                telefono = telefono,
                user = email,      // Mapeamos el email al campo 'user'
                password = contrasena
            )

            // 2. Llamamos a la API
            val response = RetrofitClient.api.registrar(nuevoUsuario)

            // 3. --- DIAGNÓSTICO DE ERRORES ---
            // Esto imprimirá en el Logcat la razón exacta si el servidor rechaza el registro
            if (!response.isSuccessful) {
                println("❌ ERROR AL REGISTRAR:")
                println("   -> Código HTTP: ${response.code()}") // Ej: 400, 409, 500
                println("   -> Mensaje: ${response.message()}") // Ej: Bad Request
                // El errorBody contiene el mensaje específico que programó tu compañero en el backend
                println("   -> Cuerpo del Error: ${response.errorBody()?.string()}")
            } else {
                println("✅ REGISTRO EXITOSO: Código ${response.code()}")
            }

            // 4. Retornamos true solo si fue exitoso (200-299)
            response.isSuccessful

        } catch (e: Exception) {
            // Esto ocurre si el servidor está apagado o no hay internet
            println("❌ ERROR DE CONEXIÓN (Excepción): ${e.message}")
            e.printStackTrace()
            false
        }
    }
}