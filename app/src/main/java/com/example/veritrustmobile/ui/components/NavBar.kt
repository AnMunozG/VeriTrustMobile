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
    val rol = SessionManager.getRol()
    val esInvitado = rol == "invitado"

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 64.dp, start = 16.dp)
    ) {
        // --- PERFIL (Solo si NO es invitado) ---
        if (!esInvitado) {
            TextButton(onClick = {
                navController.navigate(Rutas.Perfil.ruta)
                closeDrawer()
            }) {
                Text("Mi Perfil")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- SERVICIOS ---
        TextButton(onClick = {
            navController.navigate(Rutas.Servicios.ruta)
            closeDrawer()
        }) {
            Text("Servicios")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- NOSOTROS ---
        TextButton(onClick = {
            navController.navigate(Rutas.Nosotros.ruta)
            closeDrawer()
        }) {
            Text("Nosotros")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- ESTADÍSTICAS (Solo finanzas y admin) ---
        if (rol == "finanzas" || rol == "admin") {
            TextButton(onClick = {
                navController.navigate(Rutas.Estadisticas.ruta)
                closeDrawer()
            }) {
                Text("Estadísticas")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // --- BACKOFFICE (Solo admin) ---
        if (rol == "admin") {
            TextButton(onClick = {
                navController.navigate(Rutas.BackOffice.ruta)
                closeDrawer()
            }) {
                Text("BackOffice")
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(Modifier.weight(1f))

        // --- CERRAR SESIÓN / INICIAR SESIÓN ---
        if (esInvitado) {
            TextButton(onClick = {
                navController.navigate(Rutas.Acceder.ruta)
                closeDrawer()
            }) {
                Text("Iniciar Sesión")
            }
        } else {
            TextButton(onClick = {
                SessionManager.clearSession()
                navController.navigate(Rutas.Inicio.ruta) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
                closeDrawer()
            }) {
                Text("Volver a Inicio")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}