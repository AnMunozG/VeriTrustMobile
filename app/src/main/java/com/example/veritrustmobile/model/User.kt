package com.example.veritrustmobile.model

data class User(
    // Campos obligatorios para Login
    val user: String,       // Este es el email
    val password: String,

    // Campos para Registro (con valores por defecto para que no falle el Login)
    val rut: String = "",
    val nombre: String = "",
    val fechaNacimiento: String = "",
    val telefono: String = ""
)