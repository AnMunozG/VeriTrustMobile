package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.model.Servicio
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.BackOfficeUiState
import com.example.veritrustmobile.ui.viewmodel.BackOfficeViewModel

enum class DialogState {
    NONE,
    CREATE,
    EDIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(
    navController: NavController,
    viewModel: BackOfficeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var servicioParaEliminar by remember { mutableStateOf<Servicio?>(null) }

    var servicioEnEdicion by remember { mutableStateOf<Servicio?>(null) }
    var dialogState by remember { mutableStateOf(DialogState.NONE) }

    if (servicioParaEliminar != null) {
        AlertDialog(
            onDismissRequest = { servicioParaEliminar = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = "Alerta", tint = MaterialTheme.colorScheme.error) },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que quieres eliminar el servicio '${servicioParaEliminar?.nombre}'? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        servicioParaEliminar?.id?.let { viewModel.eliminarServicio(it.toString()) }
                        servicioParaEliminar = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { servicioParaEliminar = null }) { Text("Cancelar") }
            }
        )
    }

    // Diálogo para Crear o Editar Servicio
    if (dialogState != DialogState.NONE) {
        val servicioActual = if (dialogState == DialogState.EDIT) servicioEnEdicion else null
        ServicioFormDialog(
            dialogState = dialogState,
            servicio = servicioActual,
            onDismiss = { dialogState = DialogState.NONE },
            onConfirm = { servicioActualizado ->
                if (dialogState == DialogState.CREATE) {
                    viewModel.crearServicio(servicioActualizado)
                } else {
                    servicioActual?.id?.let { id ->
                        viewModel.actualizarServicio(id.toString(), servicioActualizado)
                    }
                }
                dialogState = DialogState.NONE
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administrar Servicios") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dialogState = DialogState.CREATE },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Servicio", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when (val state = uiState) {
                is BackOfficeUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is BackOfficeUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
                is BackOfficeUiState.Success -> {
                    if (state.servicios.isEmpty()) {
                        Text(
                            "No hay servicios para mostrar.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.servicios, key = { it.id ?: -1 }) { servicio ->
                                ServicioAdminCard(
                                    servicio = servicio,
                                    onEditClick = {
                                        servicioEnEdicion = servicio
                                        dialogState = DialogState.EDIT
                                    },
                                    onDeleteClick = { servicioParaEliminar = servicio }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ServicioAdminCard(
    servicio: Servicio,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(servicio.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    text = "ID: ${servicio.id ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Composable
fun ServicioFormDialog(
    dialogState: DialogState,
    servicio: Servicio?,
    onDismiss: () -> Unit,
    onConfirm: (Servicio) -> Unit
) {
    var nombre by remember { mutableStateOf(servicio?.nombre ?: "") }
    var descripcion by remember { mutableStateOf(servicio?.descripcion ?: "") }
    var precio by remember { mutableStateOf(servicio?.precio?.toString() ?: "") }
    var detalles by remember { mutableStateOf(servicio?.detalles?.joinToString(", ") ?: "") }

    val isFormValid = nombre.isNotBlank() && descripcion.isNotBlank() && precio.toIntOrNull() != null

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (dialogState == DialogState.CREATE) "Crear Nuevo Servicio" else "Editar Servicio",
                    style = MaterialTheme.typography.titleLarge
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = detalles,
                    onValueChange = { detalles = it },
                    label = { Text("Detalles (separados por coma)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val servicioActualizado = Servicio(
                                id = if (dialogState == DialogState.CREATE) null else servicio?.id,
                                nombre = nombre,
                                descripcion = descripcion,
                                precio = precio.toInt(),
                                detalles = detalles.split(",").map { it.trim() }
                            )
                            onConfirm(servicioActualizado)
                        },
                        enabled = isFormValid
                    ) {
                        Text(if (dialogState == DialogState.CREATE) "Crear" else "Guardar")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BackOfficeScreenPreview() {
    VeriTrustMobileTheme {
        BackOfficeScreen(navController = rememberNavController())
    }
}

@Preview
@Composable
fun ServicioFormDialogCreatePreview() {
    VeriTrustMobileTheme {
        ServicioFormDialog(dialogState = DialogState.CREATE, servicio = null, onDismiss = {}, onConfirm = {})
    }
}
