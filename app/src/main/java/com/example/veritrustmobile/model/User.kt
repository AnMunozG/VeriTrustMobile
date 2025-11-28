package com.example.veritrustmobile.model

data class User(
    val user: String,       // Este es el correo
    val password: String,

    val rut: String = "",
    val nombre: String = "",
    val fechaNacimiento: String = "",
    val telefono: String = ""
)