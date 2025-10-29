package com.example.veritrustmobile.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.veritrustmobile.navigation.Rutas

@Composable
fun NavBar(
    navController: NavHostController,
    closeDrawer: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Botones de navegación superiores
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp)) // Espacio desde la parte superior
            TextButton(onClick = {
                navController.navigate(Rutas.Nosotros.ruta)
                closeDrawer()
            }) {
                Text("Nosotros")
            }
            TextButton(onClick = {
                navController.navigate(Rutas.Servicios.ruta)
                closeDrawer()
            }) {
                Text("Servicios")
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el siguiente elemento hacia abajo

        // Botón inferior para cerrar sesión
        TextButton(onClick = {
            navController.navigate("Inicio") {
                // Limpia el historial de navegación para evitar volver al estado de inicio de sesión
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
                // Asegura que solo haya una instancia de la pantalla de inicio
                launchSingleTop = true
            }
            closeDrawer()
        }) {
            Text("Cerrar Sesión")
        }
    }
}
