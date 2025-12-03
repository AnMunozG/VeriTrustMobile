package com.example.veritrustmobile.data

import com.example.veritrustmobile.model.Documento
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE
interface ApiService {

    @POST("usuarios")
    suspend fun registrar(@Body request: User): Response<User>

    @POST("usuarios/login")
    suspend fun login(@Body request: User): Response<User>
    
    @GET("servicios")
    suspend fun getServicios(): Response<List<Servicio>>

    @GET("usuarios/{email}")
    suspend fun getUsuario(@Path("email") email: String): Response<User>

    @PUT("usuarios")
    suspend fun actualizarUsuario(@Body user: User): Response<User>

    @DELETE("usuarios/{email}")
    suspend fun eliminarUsuario(@Path("email") email: String): Response<Void>

    @POST("documentos")
    suspend fun guardarDocumento(@Body documento: Documento): Response<Documento>
}