package com.example.veritrustmobile.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user")       // Coincide con backend @JsonProperty("user")
    val user: String,

    @SerializedName("password")   // Coincide con backend @JsonProperty("password")
    val password: String,

    @SerializedName("rut")
    val rut: String = "",

    @SerializedName("nombre")
    val nombre: String = "",

    @SerializedName("fechaNacimiento") // Coincide con los métodos del backend
    val fechaNacimiento: String = "",

    @SerializedName("telefono")
    val telefono: String = "",

    @SerializedName("region")     // ¡Esto hará que se vea en el Perfil!
    val region: String = "",

    @SerializedName("genero")     // ¡Esto hará que se vea en el Perfil!
    val genero: String = ""
)