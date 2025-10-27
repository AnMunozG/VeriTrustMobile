package com.example.veritrustmobile.rutas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.veritrustmobile.pantallas.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Inicio.ruta
    ) {
        composable(Rutas.Inicio.ruta) { Inicio() }
        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(Rutas.Servicios.ruta) { Servicios() }
        composable(Rutas.Acceder.ruta) { Acceder(navController = navController) }
        composable(Rutas.Registro.ruta) { Registro(navController = navController) }
        composable(Rutas.Comprar.ruta) { Comprar() }

    }
}
