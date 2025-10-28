package com.example.veritrustmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.veritrustmobile.ui.screens.Acceder
import com.example.veritrustmobile.ui.screens.PantallaCompra
import com.example.veritrustmobile.ui.screens.Inicio
import com.example.veritrustmobile.ui.screens.Nosotros
import com.example.veritrustmobile.ui.screens.RegistroScreen
import com.example.veritrustmobile.ui.screens.ServiciosScreen
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Inicio.ruta
    ) {
        composable(Rutas.Inicio.ruta) { Inicio(navController = navController) }
        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(Rutas.Servicios.ruta) { ServiciosScreen(navController = navController) }
        composable(Rutas.Acceder.ruta) { Acceder(navController = navController) }
        composable(Rutas.Registro.ruta) { RegistroScreen(navController = navController) }
        composable(Rutas.Comprar.ruta) { PantallaCompra() }
    }
}

