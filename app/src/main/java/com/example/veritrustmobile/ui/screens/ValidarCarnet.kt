package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel

@Composable
fun ValidarCarnetScreen(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel()
) {
    var fotoFrontalTomada by remember { mutableStateOf(false) }
    var fotoTraseraTomada by remember { mutableStateOf(false) }

    // Escuchamos el evento de navegación: Si el registro es exitoso, nos lleva al Login
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
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Permite scroll si la pantalla es chica
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

            // --- BOTONES DE FOTOS ---
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

            // --- SECCIÓN DE ERROR Y BOTÓN FINAL ---

            // 1. Si hay un error, lo mostramos aquí en ROJO
            if (viewModel.errorRut != null) {
                Text(
                    text = viewModel.errorRut ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // 2. Si está cargando mostramos el círculo, si no, el botón
            if (viewModel.estaCargando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.finalizarRegistro() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    // Solo se habilita si ambas fotos "fueron tomadas"
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
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = if (fotoTomada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (fotoTomada) {
                    Text("✓ Capturada", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                } else {
                    Image(
                        Icons.Filled.CameraAlt,
                        contentDescription = "Cámara",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
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