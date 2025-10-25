package com.example.veritrustmobile.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.veritrustmobile.rutas.Rutas

@Composable
fun NavBar(
    navController: NavHostController,
    closeDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 64.dp, start = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        TextButton(onClick = {
            navController.navigate(Rutas.Inicio.ruta)
            closeDrawer()
        }) {
            Text("Inicio")
        }
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
        TextButton(onClick = {
            navController.navigate(Rutas.Acceder.ruta)
            closeDrawer()
        }) {
            Text("Acceder")
        }

    }
}
