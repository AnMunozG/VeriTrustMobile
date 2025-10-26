package com.example.veritrustmobile.rutas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.veritrustmobile.pantallas.*
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Rutas.Acceder.ruta
    ) {
        composable(
            route = Rutas.Inicio.ruta + "/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username")
            Inicio(username = username)
        }

        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(Rutas.Servicios.ruta) { Servicios() }

        composable(Rutas.Acceder.ruta) {
            Acceder(navController = navController)
        }

        composable(Rutas.Registro.ruta) { Registro() }
        composable(Rutas.Comprar.ruta) { Comprar() }
    }
}