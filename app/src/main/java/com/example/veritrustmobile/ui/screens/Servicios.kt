package com.example.veritrustmobile.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.ServiciosViewModel
import com.example.veritrustmobile.ui.viewmodel.UiState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.shimmer
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiciosScreen(
    navController: NavHostController,
    esInvitado: Boolean,
    viewModel: ServiciosViewModel = viewModel()
) {
    val uiState by viewModel.servicesState.collectAsState()

    Column {
        Text(
            text = "Podemos ayudar a que tu negocio crezca",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp),
            fontWeight = FontWeight.Bold
        )

        when (val state = uiState) {
            is UiState.Loading -> {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(3) {
                        ShimmerLoadingCard()
                    }
                }
            }
            is UiState.Success -> {
                val services = state.data
                if (services.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay servicios disponibles en este momento.")
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(services, key = { it.id ?: -1 }) { servicio ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                                        slideInVertically(
                                            initialOffsetY = { it / 2 },
                                            animationSpec = tween(durationMillis = 500)
                                        )
                            ) {
                                ServicioCard(
                                    servicio = servicio,
                                    onComprarClick = {
                                        val rutaCompra = Rutas.Comprar.crearRuta(
                                            nombre = servicio.nombre,
                                            precio = servicio.precio
                                        )
                                        navController.navigate(rutaCompra)
                                    },
                                    esInvitado = esInvitado
                                )
                            }
                        }
                    }
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
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
    val formatadorDeMoneda = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    formatadorDeMoneda.maximumFractionDigits = 0

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = servicio.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = servicio.descripcion,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                servicio.detalles.forEach { detalle ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = detalle,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Desde ${formatadorDeMoneda.format(servicio.precio)} + IVA",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onComprarClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !esInvitado,
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("Comprar", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun ShimmerLoadingCard() {
    val shimmerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .placeholder(
                visible = true,
                color = shimmerColor,
                highlight = PlaceholderHighlight.shimmer(highlightColor = Color.White),
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(20.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(16.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(120.dp))
        }
    }
}

@Preview(showBackground = true, name = "Pantalla de Servicios (Invitado)")
@Composable
fun ServiciosScreenPreview() {
    VeriTrustMobileTheme(dynamicColor = false) {
        Surface {
            ServiciosScreen(rememberNavController(), esInvitado = true)
        }
    }
}

