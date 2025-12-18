package com.example.veritrustmobile.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.ui.screens.*
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String
) {
    val registroViewModel: RegistroViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(500)) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(500)) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(500)) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(500)) + fadeOut() }
    ) {
        composable(
            route = Rutas.Inicio.ruta,
            arguments = listOf(navArgument("user") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user")
            Inicio(navController = navController, user = user)
        }

        composable(route = Rutas.Servicios.ruta) {
            ServiciosScreen(navController = navController)
        }

        composable(
            route = "comprar/{servicioId}/{nombreServicio}/{precioServicio}",
            arguments = listOf(
                navArgument("servicioId") { type = NavType.IntType },
                navArgument("nombreServicio") { type = NavType.StringType },
                navArgument("precioServicio") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val rol = SessionManager.getRol()

            if (rol == "invitado") {
                navController.navigate(Rutas.Acceder.ruta) {
                    popUpTo(Rutas.Servicios.ruta) { inclusive = false }
                    launchSingleTop = true
                }
            } else {
                // ⭐ 3. RECUPERAR el ID del servicio
                val id = backStackEntry.arguments?.getInt("servicioId") ?: 1
                val nombre = backStackEntry.arguments?.getString("nombreServicio") ?: "Servicio"
                val precio = backStackEntry.arguments?.getInt("precioServicio") ?: 0

                // ⭐ 4. PASAR el ID a PantallaCompra
                PantallaCompra(
                    navController = navController,
                    servicioId = id, // <-- Pasa el ID aquí
                    nombreServicio = nombre,
                    precioServicio = precio
                )
            }
        }

        composable(Rutas.FirmarDocumento.ruta) {
            FirmarDocumentoScreen(navController = navController)
        }

        composable(Rutas.Perfil.ruta) {
            val rol = SessionManager.getRol()

            if (rol == "invitado") {
                navController.navigate(Rutas.Acceder.ruta) {
                    popUpTo(Rutas.Servicios.ruta) { inclusive = false }
                    launchSingleTop = true
                }
            } else {
                PerfilScreen(navController = navController)
            }
        }

        composable(Rutas.Estadisticas.ruta) {
            val rol = SessionManager.getRol()

            if (rol == "finanzas" || rol == "admin") {
                EstadisticasScreen()
            } else {
                navController.navigate(Rutas.Servicios.ruta) {
                    popUpTo(Rutas.Servicios.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable(Rutas.BackOffice.ruta) {
            val rol = SessionManager.getRol()

            if (rol == "admin") {
                BackOfficeScreen(navController = navController)
            } else {
                navController.navigate(Rutas.Servicios.ruta) {
                    popUpTo(Rutas.Servicios.ruta) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }

        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(Rutas.Acceder.ruta) { Acceder(navController = navController) }
        composable(Rutas.RecuperarContrasena.ruta) { RecuperarContrasenaScreen(navController = navController) }
        composable(Rutas.Registro.ruta) { RegistroScreen(navController = navController, viewModel = registroViewModel) }
        composable(Rutas.ValidarCarnet.ruta) { ValidarCarnetScreen(navController = navController, viewModel = registroViewModel) }
    }
}