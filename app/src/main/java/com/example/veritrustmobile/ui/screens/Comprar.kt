package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation // Importante para quitar la máscara
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.veritrustmobile.data.ExternalRetrofitClient
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.viewmodel.CompraViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCompra(
    navController: NavController,
    nombreServicio: String = "Servicio",
    precioServicio: Int = 0,
    viewModel: CompraViewModel = viewModel()
) {
    val tasaIVA = 0.19
    val montoIva = (precioServicio * tasaIVA).roundToInt()
    val montoTotal = precioServicio + montoIva

    // Estados para la API Externa
    var valorUF by remember { mutableStateOf("Cargando...") }
    var valorDolar by remember { mutableStateOf("Cargando...") }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { success ->
            if (success) {
                navController.navigate(Rutas.FirmarDocumento.ruta) {
                    popUpTo(Rutas.Comprar.ruta) { inclusive = true }
                }
            }
        }
    }

    // Llamada a API Externa (Indicadores)
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val respuesta = ExternalRetrofitClient.api.getIndicadores()
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                valorUF = format.format(respuesta.uf.valor)
                valorDolar = format.format(respuesta.dolar.valor)
            } catch (e: Exception) {
                valorUF = "---"
                valorDolar = "---"
            }
        }
    }

    val formatador = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    formatador.maximumFractionDigits = 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen de Pago") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = nombreServicio,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Valor Neto:", style = MaterialTheme.typography.bodyLarge)
                        Text(formatador.format(precioServicio), style = MaterialTheme.typography.bodyLarge)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("IVA (19%):", style = MaterialTheme.typography.bodyLarge)
                        Text(formatador.format(montoIva), style = MaterialTheme.typography.bodyLarge)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Total a pagar:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(
                            text = formatador.format(montoTotal),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // WIDGET API EXTERNA
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Indicadores:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Row {
                        Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                            Text("UF: $valorUF", modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.labelSmall)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) {
                            Text("USD: $valorDolar", modifier = Modifier.padding(4.dp), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Datos de Tarjeta", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = viewModel.cardName,
                onValueChange = viewModel::onCardNameChange,
                label = { Text("Nombre en la tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = viewModel.cardNumber,
                onValueChange = viewModel::onCardNumberChange,
                label = { Text("Número de tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                visualTransformation = VisualTransformation.None,
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.expirationMonth,
                    onValueChange = viewModel::onExpirationMonthChange,
                    label = { Text("Mes") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )
                OutlinedTextField(
                    value = viewModel.expirationYear,
                    onValueChange = viewModel::onExpirationYearChange,
                    label = { Text("Año") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = viewModel.cvv,
                onValueChange = viewModel::onCvvChange,
                label = { Text("CVV") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                visualTransformation = VisualTransformation.None, // ¡SE VEN LOS NÚMEROS!
                singleLine = true
            )
            
            viewModel.uiMessage?.let {
                Text(
                    text = it,
                    color = if (it.contains("éxito")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.processPayment() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Pagar ${formatador.format(montoTotal)}")
                }
            }
        }
    }
}