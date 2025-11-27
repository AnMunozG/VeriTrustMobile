package com.example.veritrustmobile.data

import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: User): Response<User>

    // AGREGADO: Endpoint para registrar usuarios
    // Nota: Asegúrate que tu backend escuche en "register" o "auth/register"
    @POST("register")
    suspend fun registrar(@Body request: User): Response<User>

    @GET("servicios")
    suspend fun getServicios(): Response<List<Servicio>>
}