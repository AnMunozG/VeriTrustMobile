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
        // ANIMACIONES GLOBALES (Transiciones suaves)
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

        // SERVICIOS
        composable(
            route = Rutas.Servicios.ruta,
            arguments = listOf(navArgument("esInvitado") { type = NavType.BoolType })
        ) { backStackEntry ->
            val esInvitado = backStackEntry.arguments?.getBoolean("esInvitado") ?: true
            val serviciosViewModel: ServiciosViewModel = viewModel()
            ServiciosScreen(navController = navController, esInvitado = esInvitado, viewModel = serviciosViewModel)
        }

        // COMPRAR (Con argumentos)
        composable(
            route = "comprar/{nombreServicio}/{precioServicio}",
            arguments = listOf(
                navArgument("nombreServicio") { type = NavType.StringType },
                navArgument("precioServicio") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombreServicio") ?: "Servicio"
            val precio = backStackEntry.arguments?.getInt("precioServicio") ?: 0
            PantallaCompra(navController = navController, nombreServicio = nombre, precioServicio = precio)
        }

        // FIRMAR DOCUMENTO (Pasamos navController para el botón "Volver")
        composable(Rutas.FirmarDocumento.ruta) {
            FirmarDocumentoScreen(navController = navController)
        }

        // PERFIL (Nueva ruta)
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(navController = navController)
        }

        // OTRAS PANTALLAS
        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(Rutas.Acceder.ruta) { Acceder(navController = navController) }
        composable(Rutas.RecuperarContrasena.ruta){ RecuperarContrasenaScreen(navController = navController) }

        // REGISTRO Y VALIDACIÓN (Comparten ViewModel)
        composable(Rutas.Registro.ruta) { RegistroScreen(navController = navController, viewModel = registroViewModel) }
        composable(Rutas.ValidarCarnet.ruta) { ValidarCarnetScreen(navController = navController, viewModel = registroViewModel) }
    }
}