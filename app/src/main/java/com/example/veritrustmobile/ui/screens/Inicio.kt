package com.example.veritrustmobile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import kotlinx.coroutines.delay

@Composable
fun Inicio(navController: NavHostController, user: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp), // Aumentado para más aire
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            // Pantalla de bienvenida para usuario logueado
            LaunchedEffect(Unit) {
                delay(3000)
                navController.navigate(Rutas.Servicios.crearRuta(valorArgumento = false)) {
                    popUpTo(Rutas.Inicio.crearRuta(user)) { inclusive = true }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Bienvenido,",
                    style = MaterialTheme.typography.headlineLarge,
                    // Color por defecto 'onBackground' es correcto y legible
                )
                Text(
                    text = user,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            // Pantalla de bienvenida para invitados
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "VeriTrust",
                    style = MaterialTheme.typography.displaySmall, // Título más grande
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tu Identidad Digital",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "más segura y confiable que nunca",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Color más sutil
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Con Certificado Digital, Firma Electrónica y DNI puedes gestionar tus trámites en línea de manera rápida, sencilla y con total protección.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre botones
            ) {
                Button(
                    onClick = { navController.navigate(Rutas.Acceder.ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Iniciar Sesión") }

                OutlinedButton( // Botón secundario con estilo diferente
                    onClick = { navController.navigate(Rutas.Registro.ruta) },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Registrarse") }

                TextButton(onClick = { navController.navigate(Rutas.Servicios.crearRuta(valorArgumento = true)) }) {
                    Text("Continuar como invitado")
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun InicioPreview() {
    VeriTrustMobileTheme {
        Surface {
            Inicio(navController = rememberNavController(), user = null)
        }
    }
}
