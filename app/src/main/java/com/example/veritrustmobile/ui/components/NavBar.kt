package com.example.veritrustmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.navigation.Rutas

@Composable
fun NavBar(
    navController: NavHostController,
    closeDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 64.dp, start = 16.dp)
    ) {
        // --- 1. PERFIL (Primero en la lista) ---
        TextButton(onClick = {
            navController.navigate(Rutas.Perfil.ruta)
            closeDrawer()
        }) {
            Text("Mi Perfil")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. SERVICIOS ---
        TextButton(onClick = {
            navController.navigate(Rutas.Servicios.crearRuta(false))
            closeDrawer()
        }) {
            Text("Servicios")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 3. NOSOTROS ---
        TextButton(onClick = {
            navController.navigate(Rutas.Nosotros.ruta)
            closeDrawer()
        }) {
            Text("Nosotros")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 4. BACKOFFICE ---
        TextButton(onClick = {
            navController.navigate(Rutas.BackOffice.ruta)
            closeDrawer()
        }) {
            Text("BackOffice")
        }


        Spacer(Modifier.weight(1f))

        // --- CERRAR SESIÓN ---
        TextButton(onClick = {
            SessionManager.clearSession()
            navController.navigate(Rutas.Acceder.ruta) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
            closeDrawer()
        }) {
            Text("Cerrar Sesión")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}