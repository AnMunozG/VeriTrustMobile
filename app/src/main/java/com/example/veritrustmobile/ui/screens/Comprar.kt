package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.veritrustmobile.data.ExternalRetrofitClient // <--- Importante
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
    // 1. CÁLCULO SIMPLE DE IVA (Requisito: campos numéricos y negocio)
    val iva = (precioServicio * 0.19).roundToInt()
    val total = precioServicio + iva

    // 2. ESTADO PARA LA API EXTERNA
    var textoUF by remember { mutableStateOf("Cargando...") }
    var textoDolar by remember { mutableStateOf("Cargando...") }

    // Cargar datos de la API al iniciar
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            try {
                val datos = ExternalRetrofitClient.api.getIndicadores()
                textoUF = "$${datos.uf.valor}"
                textoDolar = "$${datos.dolar.valor}"
            } catch (e: Exception) {
                textoUF = "Error"
            }
        }
    }

    // Navegación (Tu lógica original)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { success ->
            if (success) {
                navController.navigate(Rutas.FirmarDocumento.ruta) {
                    popUpTo(Rutas.Comprar.ruta) { inclusive = true }
                }
            }
        }
    }

    val formatador = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    formatador.maximumFractionDigits = 0

    Scaffold(
        topBar = { TopAppBar(title = { Text("Finalizar Compra") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // TARJETA DE RESUMEN (Modificada para mostrar IVA)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Servicio: $nombreServicio", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Desglose de precios
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Neto:")
                        Text(formatador.format(precioServicio))
                    }
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("IVA (19%):")
                        Text(formatador.format(iva))
                    }
                    Divider(Modifier.padding(vertical = 8.dp))
                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                        Text("Total:", fontWeight = FontWeight.Bold)
                        Text(formatador.format(total), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // TARJETA PEQUEÑA PARA LA API (Requisito 36)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("Indicadores Hoy:")
                    Text("UF: $textoUF", fontWeight = FontWeight.Bold)
                    Text("Dólar: $textoDolar", fontWeight = FontWeight.Bold)
                }
            }

            Text("Datos de Pago", style = MaterialTheme.typography.titleMedium)

            // --- TU FORMULARIO ORIGINAL (Sin cambios complejos) ---
            OutlinedTextField(
                value = viewModel.cardName,
                onValueChange = viewModel::onCardNameChange,
                label = { Text("Nombre Titular") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = viewModel.cardNumber,
                onValueChange = viewModel::onCardNumberChange,
                label = { Text("Número Tarjeta") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.expirationMonth, onValueChange = viewModel::onExpirationMonthChange,
                    label = { Text("Mes") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = viewModel.expirationYear, onValueChange = viewModel::onExpirationYearChange,
                    label = { Text("Año") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = viewModel.cvv, onValueChange = viewModel::onCvvChange,
                    label = { Text("CVV") }, modifier = Modifier.weight(1f),
                    visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
                )
            }

            viewModel.uiMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(8.dp))
            }

            Button(
                onClick = { viewModel.processPayment() },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                // El botón muestra el total calculado
                Text("Pagar ${formatador.format(total)}")
            }
        }
    }
}