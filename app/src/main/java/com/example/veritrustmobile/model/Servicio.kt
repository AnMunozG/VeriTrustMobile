package com.example.veritrustmobile.model

data class Servicio(
    val id : Long?,
    val nombre: String,
    val descripcion: String,
    val precio: Int,val detalles: List<String>
)
