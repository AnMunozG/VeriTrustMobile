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
    val registroViewModel: RegistroViewModel = viewModel()

    // 1. La ruta de inicio ahora es la que tiene el argumento opcional.
    NavHost(
        navController = navController,
        startDestination = Rutas.Inicio.ruta
    ) {
        // --- Pantalla de Inicio ---
        // 2. Se define el argumento 'user' como opcional (nullable).
        composable(
            route = Rutas.Inicio.ruta,
            arguments = listOf(navArgument("user") {
                type = NavType.StringType
                nullable = true // Permite que el argumento sea nulo
                defaultValue = null // Valor por defecto si no se pasa
            })
        ) { backStackEntry ->
            // 3. Se extrae el argumento 'user' y se pasa a la pantalla Inicio.
            val user = backStackEntry.arguments?.getString("user")
            Inicio(navController = navController, user = user)
        }

        // --- Pantalla de Servicios ---
        // 4. Se define el argumento 'esInvitado' como booleano.
        composable(Rutas.Servicios.ruta){
            val esInvitado = it.arguments?.getBoolean("esInvitado") ?: false
            val serviciosViewModel: ServiciosViewModel = viewModel()

            ServiciosScreen(
                navController = navController,
                esInvitado = esInvitado,
                viewModel = serviciosViewModel
            )
        }


        // --- Resto de las pantallas (sin cambios) ---
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
    }
}
