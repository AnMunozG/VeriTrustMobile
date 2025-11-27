package com.example.veritrustmobile.repository

import com.example.veritrustmobile.model.User
// Asegúrate de importar tu RetrofitClient.
// Si lo pusiste en 'data', usa este import:
import com.example.veritrustmobile.data.RetrofitClient

class AuthRepository {

    // NOTA: Ya no necesitamos 'context' en el constructor

    /**
     * LOGIN: Envía credenciales a la API
     * (Reemplaza a la antigua función findUserByCredentials)
     */
    suspend fun login(email: String, pass: String): User? {
        return try {
            // Creamos el objeto User que espera la API
            val userRequest = User(user = email, password = pass)

            // Llamamos al endpoint definido en ApiService
            val response = RetrofitClient.api.login(userRequest)

            if (response.isSuccessful) {
                // Si es 200 OK, devolvemos el usuario que responde la API
                response.body()
            } else {
                // Si es 401 (no autorizado) u otro error
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null // Error de conexión
        }
    }

    /**
     * REGISTRO:
     * Por ahora simularemos que siempre funciona (return true)
     * porque necesitamos agregar el endpoint @POST("registro") en ApiService.
     */
    suspend fun registrarUsuario(
        rut: String,
        nombre: String,
        fechaNacimiento: String,
        telefono: String,
        email: String,
        contrasena: String
    ): Boolean {
        // TODO: Cuando tu Backend tenga lista la URL de registro,
        // aquí llamaremos a RetrofitClient.api.registrar(...)

        return true // Simulamos éxito para que puedas avanzar con la UI
    }
}