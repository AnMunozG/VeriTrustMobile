package com.example.veritrustmobile.navigation

sealed class Rutas(val ruta: String) {
    object Inicio : Rutas("Inicio/{user}") {
        fun crearRuta(user: String) = "Inicio/$user"
    }
    object Nosotros : Rutas("Nosotros")
    object Servicios : Rutas("Servicios?esInvitado={esInvitado}") {
        fun crearRuta(esInvitado: Boolean) = "Servicios?esInvitado=$esInvitado"
    }
    object Acceder : Rutas("Acceder")
    object Registro : Rutas("Registro")
    object Comprar : Rutas("Comprar")
    object RecuperarContrasena : Rutas("RecuperarContrasena")
    object ValidarCarnet : Rutas("ValidarCarnet")
}
