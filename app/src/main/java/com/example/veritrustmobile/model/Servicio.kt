package com.example.veritrustmobile.model

data class Servicio(
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val detalles: List<String>
)