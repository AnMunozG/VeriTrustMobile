package com.example.veritrustmobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.veritrustmobile.SessionManager // Importar el Manager
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
        TextButton(onClick = {
            navController.navigate(Rutas.Servicios.crearRuta(false))
            closeDrawer()
        }) {
            Text("Servicios")
        }

        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = {
            navController.navigate(Rutas.Nosotros.ruta)
            closeDrawer()
        }) {
            Text("Nosotros")
        }


        Spacer(Modifier.weight(1f))

        TextButton(onClick = {
            // 1. BORRAR SESIÓN
            SessionManager.clearSession()

            // 2. IR AL LOGIN Y BORRAR HISTORIAL
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