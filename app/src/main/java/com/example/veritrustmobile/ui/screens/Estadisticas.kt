package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen() {
    val formatador = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    formatador.maximumFractionDigits = 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas de Ventas") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Panel de Control Financiero",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // CARDS DE RESUMEN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    titulo = "Ventas Totales",
                    valor = formatador.format(12500000),
                    icono = Icons.Default.AttachMoney,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    titulo = "Órdenes",
                    valor = "342",
                    icono = Icons.Default.ShoppingCart,
                    modifier = Modifier.weight(1f)
                )
            }

            StatCard(
                titulo = "Crecimiento Mensual",
                valor = "+18.5%",
                icono = Icons.Default.TrendingUp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // TABLA DE PRODUCTOS MÁS VENDIDOS
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Servicios Más Vendidos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ProductRow("Firma Electrónica Simple", 145, formatador.format(2232050))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProductRow("Firma Electrónica Avanzada", 98, formatador.format(2155020))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    ProductRow("Firma Online - Empresa en un Día", 99, formatador.format(79002))
                }
            }

            // INFORMACIÓN ADICIONAL
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Nota: Los datos mostrados corresponden al mes en curso.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun StatCard(
    titulo: String,
    valor: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ProductRow(nombre: String, cantidad: Int, total: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(nombre, style = MaterialTheme.typography.bodyLarge)
            Text(
                "$cantidad unidades vendidas",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            total,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EstadisticasScreenPreview() {
    VeriTrustMobileTheme {
        EstadisticasScreen()
    }
}