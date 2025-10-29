package com.example.veritrustmobile.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.veritrustmobile.ui.screens.Acceder
import com.example.veritrustmobile.ui.screens.Inicio
import com.example.veritrustmobile.ui.screens.Nosotros
import com.example.veritrustmobile.ui.screens.PantallaCompra
import com.example.veritrustmobile.ui.screens.RecuperarContrasenaScreen
import com.example.veritrustmobile.ui.screens.RegistroScreen
import com.example.veritrustmobile.ui.screens.ServiciosScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "Inicio"
    ) {
        composable("Inicio") { 
            Inicio(navController = navController, user = null)
        }
        composable(
            Rutas.Inicio.ruta, 
            arguments = listOf(navArgument("user") { type = NavType.StringType })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user")
            Inicio(navController = navController, user = user)
        }
        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(
            Rutas.Servicios.ruta,
            arguments = listOf(navArgument("esInvitado") { 
                type = NavType.BoolType
                defaultValue = true
            })
        ) { backStackEntry ->
            val esInvitado = backStackEntry.arguments?.getBoolean("esInvitado") ?: true
            ServiciosScreen(navController = navController, esInvitado = esInvitado)
        }
        composable(Rutas.Acceder.ruta) { Acceder(navController = navController) }
        composable(Rutas.Registro.ruta) { RegistroScreen(navController = navController) }
        composable(Rutas.Comprar.ruta) { PantallaCompra() }
        composable(Rutas.RecuperarContrasena.ruta) { RecuperarContrasenaScreen(navController = navController) }
    }
}
