package com.example.veritrustmobile.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user")
    val user: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("rut")
    val rut: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("fechaNacimiento")
    val fechaNacimiento: String = "",

    @SerializedName("telefono")
    val telefono: String = "",

    @SerializedName("region")
    val region: String = "",

    @SerializedName("genero")
    val genero: String = "",

    // ⭐ NUEVO: Campo de rol
    @SerializedName("rol")
    val rol: String = "user"
)