package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.veritrustmobile.navigation.Rutas

@Composable
fun ValidarCarnetScreen(navController: NavController) {
    var fotoFrontalTomada by remember { mutableStateOf(false) }
    var fotoTraseraTomada by remember { mutableStateOf(false) }
    var validando by remember { mutableStateOf(false) }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Validación de Identidad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por favor, toma una foto de la parte frontal y trasera de tu carnet de identidad.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Simulación de captura de fotos
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                FotoCarnet(texto = "Parte Frontal", fotoTomada = fotoFrontalTomada) { fotoFrontalTomada = true }
                FotoCarnet(texto = "Parte Trasera", fotoTomada = fotoTraseraTomada) { fotoTraseraTomada = true }
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (validando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = {
                        validando = true
                        // Aquí iría la lógica de validación de las imágenes
                        // Por ahora, navegamos a la pantalla de inicio tras un pequeño retraso
                        navController.navigate(Rutas.Acceder.ruta) {
                            popUpTo(Rutas.Registro.ruta) { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
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
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(
                        width = 2.dp,
                        color = if (fotoTomada) MaterialTheme.colorScheme.primary else Color.Gray,
                        shape = MaterialTheme.shapes.medium
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (fotoTomada) {
                    Text("✓ Foto Capturada", color = MaterialTheme.colorScheme.primary)
                } else {
                    Image(Icons.Filled.CameraAlt
                        , contentDescription = "Cámara")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = alTomarFoto) {
            Text(texto)
        }
    }
}
