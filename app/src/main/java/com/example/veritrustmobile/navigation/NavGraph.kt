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
fun NavGraph(
    navController: NavHostController,
    startDestination: String // Parámetro para manejar la sesión
) {
    val registroViewModel: RegistroViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
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

        composable(
            route = Rutas.Servicios.ruta,
            arguments = listOf(navArgument("esInvitado") {
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            val esInvitado = backStackEntry.arguments?.getBoolean("esInvitado") ?: true
            val serviciosViewModel: ServiciosViewModel = viewModel()

            ServiciosScreen(
                navController = navController,
                esInvitado = esInvitado,
                viewModel = serviciosViewModel
            )
        }

        // RUTA COMPRAR (CON ARGUMENTOS)
        composable(
            route = "comprar/{nombreServicio}/{precioServicio}",
            arguments = listOf(
                navArgument("nombreServicio") { type = NavType.StringType },
                navArgument("precioServicio") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombreServicio") ?: "Servicio"
            val precio = backStackEntry.arguments?.getInt("precioServicio") ?: 0

            PantallaCompra(
                navController = navController,
                nombreServicio = nombre,
                precioServicio = precio
            )
        }

        // RUTA FIRMAR DOCUMENTO
        composable(Rutas.FirmarDocumento.ruta) {
            FirmarDocumentoScreen()
        }

        composable(Rutas.Nosotros.ruta) { Nosotros() }
        composable(Rutas.Acceder.ruta) { Acceder(navController = navController) }
        composable(Rutas.Registro.ruta) { RegistroScreen(navController = navController, viewModel = registroViewModel) }
        composable(Rutas.ValidarCarnet.ruta) { ValidarCarnetScreen(navController = navController, viewModel = registroViewModel) }
        composable(Rutas.RecuperarContrasena.ruta){ RecuperarContrasenaScreen(navController = navController) }

        // Si creaste la ruta de perfil, agrégala también:
        // composable(Rutas.Perfil.ruta) { PerfilScreen(navController = navController) }
    }
}