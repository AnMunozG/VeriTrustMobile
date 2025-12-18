package com.example.veritrustmobile.model

import com.google.gson.annotations.SerializedName

data class Compra(
    @SerializedName("id")
    val id: Long? = null,

    @SerializedName("usuarioId")
    val usuarioId: Long,

    @SerializedName("servicioId")
    val servicioId: Int,

    @SerializedName("fechaCompra")
    val fechaCompra: String,

    @SerializedName("montoTotal")
    val montoTotal: Double,

    @SerializedName("metodoPago")
    val metodoPago: String
)