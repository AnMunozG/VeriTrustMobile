package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.model.listaDeServicios
import com.example.veritrustmobile.navigation.Rutas
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ServiciosScreen(navController: NavHostController, esInvitado: Boolean) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Podemos ayudar a que tu negocio crezca",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(listaDeServicios) { servicio ->
            ServicioCard(
                servicio = servicio,
                onComprarClick = {
                    navController.navigate(Rutas.Comprar.ruta)
                    println("Navegando a la compra de: ${servicio.nombre}")
                },
                esInvitado = esInvitado
            )
        }
    }
}

@Composable
fun ServicioCard(
    servicio: Servicio,
    onComprarClick: () -> Unit,
    esInvitado: Boolean,
    modifier: Modifier = Modifier
) {
    val formatadorDeMoneda = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CL"))

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = servicio.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = servicio.descripcion,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                servicio.detalles.forEach { detalle ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null, // Icono decorativo
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = detalle,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Desde $${formatadorDeMoneda.format(servicio.precio)} + IVA",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onComprarClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !esInvitado
            ) {
                Text("Comprar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServiciosScreenPreview() {
    val navController = rememberNavController()
    com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme(dynamicColor = false) {
        ServiciosScreen(navController, esInvitado = true)
    }
}

@Preview(showBackground = true)
@Composable
fun ServicioCardPreview() {
    com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme(dynamicColor = false) {
        ServicioCard(
            servicio = listaDeServicios.first(),
            onComprarClick = {},
            esInvitado = true
        )
    }
}
