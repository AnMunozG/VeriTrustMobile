package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun RegistroScreen(
    navController: NavHostController,
    viewModel: RegistroViewModel = viewModel()
) {
    // Escuchar eventos de navegación del ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RegistroViewModel.NavigationEvent.NavigateToLogin -> {
                    navController.navigate(Rutas.Acceder.ruta) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
                is RegistroViewModel.NavigationEvent.NavigateToValidarCarnet -> {
                    navController.navigate(Rutas.ValidarCarnet.ruta)
                }
            }
        }
    }
    RegistroContent(viewModel = viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroContent(viewModel: RegistroViewModel) {

    // --- LÓGICA DATE PICKER (FECHA) ---
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Formateamos la fecha a dd/MM/yyyy para enviar al backend
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        formatter.timeZone = TimeZone.getTimeZone("UTC")
                        viewModel.onFechaNacimientoChange(formatter.format(Date(millis)))
                    }
                    showDatePicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    // --- LÓGICA DROPDOWN (REGIÓN) ---
    var expandedDropdown by remember { mutableStateOf(false) }
    val regiones = listOf("Arica y Parinacota", "Antofagasta", "Atacama", "Coquimbo", "Valparaíso", "Metropolitana", "O'Higgins", "Maule", "Biobío", "Araucanía", "Los Ríos", "Los Lagos", "Aysén", "Magallanes")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // RUT
        CampoDeTextoFormulario(
            valor = viewModel.rut,
            onValueChange = viewModel::onRutChange,
            etiqueta = "RUT (Ej: 12345678-9)",
            error = viewModel.errorRut
        )
        // NOMBRE
        CampoDeTextoFormulario(
            valor = viewModel.nombre,
            onValueChange = viewModel::onNombreChange,
            etiqueta = "Nombre completo",
            error = viewModel.errorNombre
        )

        // --- 1. FECHA DE NACIMIENTO (Campo ReadOnly + Icono Calendario) ---
        OutlinedTextField(
            value = viewModel.fechaNacimiento,
            onValueChange = {},
            label = { Text("Fecha de Nacimiento") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .clickable { showDatePicker = true }, // Click en todo el campo abre el selector
            enabled = false, // Deshabilitar teclado, pero mantener evento click en modifier
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            shape = RoundedCornerShape(12.dp),
            isError = viewModel.errorFechaNacimiento != null,
            supportingText = {
                if (viewModel.errorFechaNacimiento != null) {
                    Text(text = viewModel.errorFechaNacimiento!!, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        // TELÉFONO
        CampoDeTextoFormulario(
            valor = viewModel.telefono,
            onValueChange = viewModel::onTelefonoChange,
            etiqueta = "Teléfono",
            error = viewModel.errorTelefono,
            tipoDeTeclado = KeyboardType.Phone
        )

        // --- 2. LISTA DE VALORES (DROPDOWN - REGIÓN) ---
        ExposedDropdownMenuBox(
            expanded = expandedDropdown,
            onExpandedChange = { expandedDropdown = it },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            OutlinedTextField(
                value = viewModel.region,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                isError = viewModel.errorRegion != null,
                supportingText = {
                    if (viewModel.errorRegion != null) Text(text = viewModel.errorRegion!!, color = MaterialTheme.colorScheme.error)
                }
            )
            ExposedDropdownMenu(
                expanded = expandedDropdown,
                onDismissRequest = { expandedDropdown = false }
            ) {
                regiones.forEach { region ->
                    DropdownMenuItem(
                        text = { Text(region) },
                        onClick = {
                            viewModel.onRegionChange(region)
                            expandedDropdown = false
                        }
                    )
                }
            }
        }

        // --- 3. RADIO BUTTONS (GÉNERO) ---
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Text("Género", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = viewModel.genero == "Masculino",
                    onClick = { viewModel.onGeneroChange("Masculino") }
                )
                Text("Masculino", modifier = Modifier.clickable { viewModel.onGeneroChange("Masculino") })

                Spacer(modifier = Modifier.width(16.dp))

                RadioButton(
                    selected = viewModel.genero == "Femenino",
                    onClick = { viewModel.onGeneroChange("Femenino") }
                )
                Text("Femenino", modifier = Modifier.clickable { viewModel.onGeneroChange("Femenino") })
            }
            if (viewModel.errorGenero != null) {
                Text(text = viewModel.errorGenero!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }
        }

        // EMAIL Y PASSWORD
        CampoDeTextoFormulario(
            valor = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            etiqueta = "Correo electrónico",
            error = viewModel.errorEmail,
            tipoDeTeclado = KeyboardType.Email
        )
        CampoDeTextoFormulario(
            valor = viewModel.confirmarEmail,
            onValueChange = viewModel::onConfirmarEmailChange,
            etiqueta = "Confirma tu correo",
            error = viewModel.errorConfirmarEmail,
            tipoDeTeclado = KeyboardType.Email
        )
        CampoDeTextoFormulario(
            valor = viewModel.contrasena,
            onValueChange = viewModel::onContrasenaChange,
            etiqueta = "Contraseña",
            error = viewModel.errorContrasena,
            esContrasena = true,
            tipoDeTeclado = KeyboardType.Password
        )
        CampoDeTextoFormulario(
            valor = viewModel.confirmarContrasena,
            onValueChange = viewModel::onConfirmarContrasenaChange,
            etiqueta = "Confirma contraseña",
            error = viewModel.errorConfirmarContrasena,
            esContrasena = true,
            tipoDeTeclado = KeyboardType.Password
        )

        // CHECKBOX TÉRMINOS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable { viewModel.onTerminosChange(!viewModel.terminosAceptados) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.terminosAceptados,
                onCheckedChange = { viewModel.onTerminosChange(it) }
            )
            Text(
                text = "Acepto los términos y condiciones",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        viewModel.errorTerminos?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // BOTÓN CONTINUAR
        Button(
            onClick = viewModel::onRegistroSubmit,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Continuar")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// COMPONENTE AUXILIAR PARA LOS TEXT FIELDS
@Composable
private fun CampoDeTextoFormulario(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    esContrasena: Boolean = false,
    tipoDeTeclado: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.padding(bottom = 8.dp)) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            label = { Text(etiqueta) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = tipoDeTeclado),
            isError = error != null,
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }
}