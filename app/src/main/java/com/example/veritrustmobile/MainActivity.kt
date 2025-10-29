package com.example.veritrustmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.ui.components.NavBar
import com.example.veritrustmobile.navigation.NavGraph
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VeriTrustMobileTheme(dynamicColor = false) {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val noNavRoutes = listOf(
        Rutas.Acceder.ruta,
        Rutas.Registro.ruta,
        Rutas.RecuperarContrasena.ruta,
        Rutas.Inicio.ruta
    )

    val showNav = currentRoute?.startsWith("Inicio") == false && currentRoute !in noNavRoutes

    if (showNav) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    NavBar(
                        navController = navController,
                        closeDrawer = { scope.launch { drawerState.close() } }
                    )
                }
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("VeriTrust Mobile") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    NavGraph(navController = navController)
                }
            }
        }
    } else {
        NavGraph(navController = navController)
    }
}
