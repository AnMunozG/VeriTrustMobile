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
import com.example.veritrustmobile.navigation.NavGraph
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.components.NavBar
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader // <--- IMPORTANTE
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SessionManager.init(this)

        PDFBoxResourceLoader.init(applicationContext)

        val startDestination = if (SessionManager.getToken() != null) {
            Rutas.Servicios.crearRuta(false)
        } else {
            Rutas.Inicio.ruta
        }

        enableEdgeToEdge()
        setContent {
            VeriTrustMobileTheme(dynamicColor = false) {
                MainScreen(startDestination)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(startDestination: String) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val noNavRoutes = listOf(
        Rutas.Acceder.ruta,
        Rutas.Registro.ruta,
        Rutas.RecuperarContrasena.ruta,
        Rutas.ValidarCarnet.ruta
    )

    val showNav = currentRoute != null &&
            !currentRoute.startsWith("inicio") &&
            noNavRoutes.none { currentRoute == it }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = showNav,
        drawerContent = {
            if (showNav) {
                ModalDrawerSheet {
                    NavBar(
                        navController = navController,
                        closeDrawer = { scope.launch { drawerState.close() } }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showNav) {
                    TopAppBar(
                        title = { Text("VeriTrust Mobile") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                            }
                        }
                    )
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                NavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }
}