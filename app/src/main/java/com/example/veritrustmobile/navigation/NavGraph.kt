package com.example.veritrustmobile.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.ui.screens.*
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel
import com.example.veritrustmobile.ui.viewmodel.ServiciosViewModel

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
        // INICIO
        composable(
            route = Rutas.Inicio.ruta,
            arguments = listOf(navArgument("user") {
                type = NavType.StringType; nullable = true; defaultValue = null
            })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user")
            Inicio(navController = navController, user = user)
        }

        // ⭐ SERVICIOS - SIN ARGUMENTOS, USA SessionManager directamente
        composable(route = Rutas.Servicios.ruta) {
            val serviciosViewModel: ServiciosViewModel = viewModel()
            ServiciosScreen(
                navController = navController,
                viewModel = serviciosViewModel
            )
        }

        // ⭐ COMPRAR - Control de acceso mejorado
        composable(
            route = "comprar/{nombreServicio}/{precioServicio}",
            arguments = listOf(
                navArgument("nombreServicio") { type = NavType.StringType },
                navArgument("precioServicio") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val rol = SessionManager.getRol()

            if (rol == "invitado") {
                // ⭐ IMPORTANTE: Usar LaunchedEffect para evitar recomposiciones infinitas
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.Acceder.ruta) {
                        popUpTo(Rutas.Servicios.ruta) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            } else {
                val nombre = backStackEntry.arguments?.getString("nombreServicio") ?: "Servicio"
                val precio = backStackEntry.arguments?.getInt("precioServicio") ?: 0
                PantallaCompra(navController = navController, nombreServicio = nombre, precioServicio = precio)
            }
        }

        composable(Rutas.FirmarDocumento.ruta) {
            FirmarDocumentoScreen(navController = navController)
        }

        // ⭐ PERFIL - Con protección de acceso
        composable(Rutas.Perfil.ruta) {
            val rol = SessionManager.getRol()

            if (rol == "invitado") {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.Acceder.ruta) {
                        popUpTo(Rutas.Servicios.ruta) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            } else {
                PerfilScreen(navController = navController)
            }
        }

        // ⭐ ESTADÍSTICAS - Solo finanzas y admin
        composable(Rutas.Estadisticas.ruta) {
            val rol = SessionManager.getRol()

            if (rol == "finanzas" || rol == "admin") {
                EstadisticasScreen()
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.Servicios.ruta) {
                        popUpTo(Rutas.Servicios.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }

        // ⭐ BACKOFFICE - Solo admin
        composable(Rutas.BackOffice.ruta) {
            val rol = SessionManager.getRol()

            if (rol == "admin") {
                BackOfficeScreen(navController = navController)
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(Rutas.Servicios.ruta) {
                        popUpTo(Rutas.Servicios.ruta) { inclusive = true }
                        launchSingleTop = true
                    }
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