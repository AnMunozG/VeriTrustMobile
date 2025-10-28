package com.example.veritrustmobile.model

data class User (
    val user : String,
    val password : String
)
// Usuarios de prueba
val accounts = listOf(
    User(user = "Dilan", password = "dilan123"),
    User("Angel", password = "angel123"),
    User(user = "aadmin", password = "admin")
)