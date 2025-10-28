package com.example.veritrustmobile.navigation

sealed class Rutas(val ruta: String) {
    object Inicio : Rutas("Inicio")
    object Nosotros : Rutas("Nosotros")
    object Servicios : Rutas("Servicios")
    object Acceder : Rutas("Acceder")
    object Registro : Rutas("Registro")
    object Comprar : Rutas("Comprar")
}
