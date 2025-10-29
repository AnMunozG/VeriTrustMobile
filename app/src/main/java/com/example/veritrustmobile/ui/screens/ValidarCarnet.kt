package com.example.veritrustmobile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel

@Composable
fun ValidarCarnetScreen(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel()
) {
    var fotoFrontalTomada by remember { mutableStateOf(false) }
    var fotoTraseraTomada by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            if (event is RegistroViewModel.NavigationEvent.NavigateToLogin) {
                navController.navigate(Rutas.Acceder.ruta) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background // Fondo temático
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Validación de Identidad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor, toma una foto de la parte frontal y trasera de tu carnet de identidad.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                FotoCarnet(texto = "Parte Frontal", fotoTomada = fotoFrontalTomada) {
                    fotoFrontalTomada = true
                }
                FotoCarnet(texto = "Parte Trasera", fotoTomada = fotoTraseraTomada) {
                    fotoTraseraTomada = true
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (viewModel.estaCargando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.finalizarRegistro() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    enabled = fotoFrontalTomada && fotoTraseraTomada
                ) {
                    Text("Finalizar Validación")
                }
            }
        }
    }
}

@Composable
private fun FotoCarnet(texto: String, fotoTomada: Boolean, alTomarFoto: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(width = 150.dp, height = 100.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Color temático
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = if (fotoTomada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline, // Colores temáticos
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (fotoTomada) {
                    Text("✓ Foto Capturada", color = MaterialTheme.colorScheme.primary)
                } else {
                    Image(
                        Icons.Filled.CameraAlt,
                        contentDescription = "Cámara",
                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = alTomarFoto) {
            Text(texto)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ValidarCarnetScreenPreview() {
    VeriTrustMobileTheme {
        Surface {
            ValidarCarnetScreen(navController = rememberNavController())
        }
    }
}
