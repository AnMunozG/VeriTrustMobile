package com.example.veritrustmobile.navigation

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
fun NavGraph(navController: NavHostController) {
    // ViewModel compartido entre Registro y ValidarCarnet
    val registroViewModel: RegistroViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Rutas.Inicio.ruta
    ) {
        // --- 1. PANTALLA DE INICIO ---
        composable(
            route = Rutas.Inicio.ruta,
            arguments = listOf(navArgument("user") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user")
            Inicio(navController = navController, user = user)
        }

        // --- 2. PANTALLA DE SERVICIOS (Optimizado) ---
        composable(
            route = Rutas.Servicios.ruta, // Usa la referencia segura del objeto Rutas
            arguments = listOf(navArgument("esInvitado") {
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            val esInvitado = backStackEntry.arguments?.getBoolean("esInvitado") ?: true

            // ViewModel exclusivo para esta pantalla
            val serviciosViewModel: ServiciosViewModel = viewModel()

            ServiciosScreen(
                navController = navController,
                esInvitado = esInvitado,
                viewModel = serviciosViewModel
            )
        }

        // --- 3. PANTALLAS SIMPLES ---
        composable(Rutas.Nosotros.ruta) {
            Nosotros()
        }

        composable(Rutas.Acceder.ruta) {
            Acceder(navController = navController)
        }

        composable(Rutas.Registro.ruta) {
            RegistroScreen(navController = navController, viewModel = registroViewModel)
        }

        composable(Rutas.ValidarCarnet.ruta) {
            ValidarCarnetScreen(navController = navController, viewModel = registroViewModel)
        }

        composable(Rutas.Comprar.ruta) {
            PantallaCompra()
        }

        composable(Rutas.RecuperarContrasena.ruta){
            RecuperarContrasenaScreen(navController = navController)
        }
        composable(Rutas.FirmarDocumento.ruta) {
            FirmarDocumentoScreen()
        }
    }
}