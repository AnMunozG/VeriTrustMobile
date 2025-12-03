package com.example.veritrustmobile.model

import com.google.gson.annotations.SerializedName

data class Documento(
    @SerializedName("nombreArchivo")
    val nombreArchivo: String,

    @SerializedName("tipo")
    val tipo: String = "Contrato PDF",

    @SerializedName("direccionArchivo")
    val direccionArchivo: String, // Guardamos la URI local como referencia

    @SerializedName("fechaFirmado")
    val fechaFirmado: String? = null,

    @SerializedName("usuario")
    val usuario: User // Enviamos un objeto usuario con solo el email
)