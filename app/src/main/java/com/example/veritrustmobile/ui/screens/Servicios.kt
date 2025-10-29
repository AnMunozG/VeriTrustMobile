package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.ServiciosViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ServiciosScreen(
    navController: NavHostController,
    esInvitado: Boolean,
    viewModel: ServiciosViewModel = viewModel()
) {

    val services by viewModel.servicesState.collectAsState()

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

        // 3. Verifica si la lista de servicios está vacía (mientras se carga).
        if (services.isEmpty()) {
            item {
                // Muestra un indicador de carga en el centro.
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // 4. Cuando los datos llegan, itera sobre la lista del ViewModel.
            items(services) { servicio ->
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
}

@Composable
fun ServicioCard(
    servicio: Servicio,
    onComprarClick: () -> Unit,
    esInvitado: Boolean,
    modifier: Modifier = Modifier
) {
    val formatadorDeMoneda = NumberFormat.getNumberInstance(Locale("es", "CL"))

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

// 5. Para las Previews, creamos una lista de datos falsos porque no tienen acceso
//    a la base de datos ni al ViewModel en tiempo de diseño.
private val previewServices = listOf(
    Servicio("Firma Electrónica Simple", "Certificado Digital", 15390, listOf("Detalle 1", "Detalle 2")),
    Servicio("Firma Electrónica Avanzada", "e-token", 21990, listOf("Detalle A", "Detalle B"))
)

@Preview(showBackground = true)
@Composable
fun ServiciosScreenPreview() {
    VeriTrustMobileTheme(dynamicColor = false) {
        ServiciosScreen(rememberNavController(), esInvitado = true)
    }
}

@Preview(showBackground = true)
@Composable
fun ServicioCardPreview() {
    VeriTrustMobileTheme(dynamicColor = false) {
        ServicioCard(
            servicio = previewServices.first(),
            onComprarClick = {},
            esInvitado = true
        )
    }
}
