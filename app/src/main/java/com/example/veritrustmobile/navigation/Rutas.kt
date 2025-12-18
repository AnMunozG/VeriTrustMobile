package com.example.veritrustmobile.navigation

sealed class Rutas(val rutaBase: String, val argumento: String? = null) {
    val ruta: String = if (argumento != null) "$rutaBase/{$argumento}" else rutaBase

    object Acceder : Rutas("acceder")
    object Nosotros : Rutas("nosotros")
    object Registro : Rutas("registro")
    object ValidarCarnet : Rutas("validar_carnet")
    object RecuperarContrasena : Rutas("recuperar_contrasena")
    object Inicio : Rutas("inicio", "user")

    object Servicios : Rutas("servicios")

    object FirmarDocumento : Rutas("firmar_documento")
    object Perfil : Rutas("perfil")
    object BackOffice : Rutas("backoffice")
    object Estadisticas : Rutas("estadisticas")

    object Comprar : Rutas("comprar/{servicioId}/{nombreServicio}/{precioServicio}") {
        fun crearRuta(servicioId: Int,nombre: String, precio: Int): String {
            return "comprar/$servicioId/$nombre/$precio"
        }
    }

    fun crearRuta(valorArgumento: Any): String {
        return "$rutaBase/$valorArgumento"
    }
}