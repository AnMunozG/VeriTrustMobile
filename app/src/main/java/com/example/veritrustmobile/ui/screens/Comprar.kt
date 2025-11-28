package com.example.veritrustmobile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.CompraViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCompra(
    navController: NavController,
    nombreServicio: String = "Servicio", // Valor por defecto
    precioServicio: Int = 0,             // Valor por defecto
    viewModel: CompraViewModel = viewModel()
) {
    // 1. ESCUCHAR EVENTO DE ÉXITO PARA NAVEGAR A FIRMAR
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { success ->
            if (success) {
                // Navegamos a la firma y borramos la pantalla de compra del historial
                navController.navigate(Rutas.FirmarDocumento.ruta) {
                    popUpTo(Rutas.Comprar.ruta) { inclusive = true }
                }
            }
        }
    }

    val formatador = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    formatador.maximumFractionDigits = 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Finalizar Compra") },
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
            // 2. MOSTRAR RESUMEN DEL SERVICIO SELECCIONADO
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estás contratando:", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = nombreServicio,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total a pagar:", style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = formatador.format(precioServicio),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Ingresa los datos de pago", style = MaterialTheme.typography.headlineSmall)

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
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.expirationMonth,
                    onValueChange = viewModel::onExpirationMonthChange,
                    label = { Text("Mes (MM)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                    singleLine = true
                )
                OutlinedTextField(
                    value = viewModel.expirationYear,
                    onValueChange = viewModel::onExpirationYearChange,
                    label = { Text("Año (AAAA)") },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
                visualTransformation = PasswordVisualTransformation(),
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
                    Text("Pagar ${formatador.format(precioServicio)}")
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Composable
fun PreviewPantallaCompra() {
    VeriTrustMobileTheme {
        Surface {
            PantallaCompra(navController = rememberNavController())
        }
    }
}