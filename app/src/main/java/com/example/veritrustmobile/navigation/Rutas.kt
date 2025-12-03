package com.example.veritrustmobile.navigation

sealed class Rutas(val rutaBase: String, val argumento: String? = null) {
    val ruta: String = if (argumento != null) "$rutaBase/{$argumento}" else rutaBase

    object Acceder : Rutas("acceder")
    object Nosotros : Rutas("nosotros")
    object Registro : Rutas("registro")
    object ValidarCarnet : Rutas("validar_carnet")
    object RecuperarContrasena : Rutas("recuperar_contrasena")
    object Inicio : Rutas("inicio", "user")
    object Servicios : Rutas("servicios", "esInvitado")
    object FirmarDocumento : Rutas("firmar_documento")

    object Perfil : Rutas("perfil")

    object BackOffice : Rutas("backoffice")

    object Comprar : Rutas("comprar/{nombreServicio}/{precioServicio}") {
        fun crearRuta(nombre: String, precio: Int): String {
            return "comprar/$nombre/$precio"
        }
    }

    fun crearRuta(valorArgumento: Any): String {
        return "$rutaBase/$valorArgumento"
    }
}