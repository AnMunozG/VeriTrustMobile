package com.example.veritrustmobile.pantallas

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Inicio(username: String?) {
    Text(text = "Bienvenido, $username")
}