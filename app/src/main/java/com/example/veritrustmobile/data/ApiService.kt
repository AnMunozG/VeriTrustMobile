package com.example.veritrustmobile.data

import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // ==========================================
    // CONEXIÓN CON: UsuarioController
    // Ruta base en Java: @RequestMapping("/usuarios")
    // ==========================================

    // 1. REGISTRO
    // En Java tienes: @PostMapping public Usuario createUsuario(...)
    // Eso significa que la ruta es POST a "/usuarios" (sin nada más)
    @POST("usuarios")
    suspend fun registrar(@Body request: User): Response<User>

    // 2. LOGIN (OJO: LEER NOTA ABAJO)
    // Tu controlador Java NO tiene login actualmente.
    // Vamos a apuntar a esta ruta y luego te doy el código para agregarla en Java.
    @POST("usuarios/login")
    suspend fun login(@Body request: User): Response<User>


    // ==========================================
    // CONEXIÓN CON: ServicioController
    // Ruta base en Java: @RequestMapping("/servicios")
    // ==========================================

    // 3. LISTAR SERVICIOS
    // En Java tienes: @GetMapping public List<Servicio> getAllServicios()
    // Eso significa que la ruta es GET a "/servicios"
    @GET("servicios")
    suspend fun getServicios(): Response<List<Servicio>>
}