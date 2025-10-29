package com.example.veritrustmobile.navigation

sealed class Rutas(val rutaBase: String, val argumento: String? = null) {
    val ruta: String = if (argumento != null) "$rutaBase/{$argumento}" else rutaBase

    // Rutas sin argumentos
    object Acceder : Rutas("acceder")
    object Nosotros : Rutas("nosotros")
    object Comprar : Rutas("comprar")
    object Registro : Rutas("registro")
    object ValidarCarnet : Rutas("validar_carnet")

    object RecuperarContrasena : Rutas("recuperar_contrasena")
    object Inicio : Rutas("inicio", "user") // Define el argumento 'user'
    object Servicios : Rutas("servicios", "esInvitado") // Define el argumento 'esInvitado'

    fun crearRuta(valorArgumento: Any): String {
        return "$rutaBase/$valorArgumento"
    }
}
