package com.example.veritrustmobile.navigation

// Clase sellada para definir las rutas de navegación de la aplicación.
sealed class Rutas(val rutaBase: String, val argumento: String? = null) {
    // La ruta completa que se usará en el NavGraph.
    val ruta: String = if (argumento != null) "$rutaBase/{$argumento}" else rutaBase

    // Rutas sin argumentos
    object Acceder : Rutas("acceder")
    object Nosotros : Rutas("nosotros")
    object Comprar : Rutas("comprar")
    object Registro : Rutas("registro")
    object ValidarCarnet : Rutas("validar_carnet")

    object RecuperarContrasena : Rutas("recuperar_contrasena")

    // Rutas con argumentos
    object Inicio : Rutas("inicio", "user") // Define el argumento 'user'
    object Servicios : Rutas("servicios", "esInvitado") // Define el argumento 'esInvitado'

    /**
     * Función para construir la ruta con el valor del argumento.
     * Ejemplo: Rutas.Inicio.crearRuta("dilan@test.com") -> "inicio/dilan@test.com"
     */
    fun crearRuta(valorArgumento: Any): String {
        return "$rutaBase/$valorArgumento"
    }
}
