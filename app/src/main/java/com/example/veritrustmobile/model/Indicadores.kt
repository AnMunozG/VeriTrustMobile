package com.example.veritrustmobile.model

data class MindicadorResponse(
    val uf: Indicador,
    val dolar: Indicador
)

data class Indicador(
    val valor: Double,
    val fecha: String
)