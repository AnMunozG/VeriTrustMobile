package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.viewmodel.PerfilViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    viewModel: PerfilViewModel = viewModel()
) {
    // Estado local para controlar el cuadro de diálogo de eliminación
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // EFECTO: Si el ViewModel reporta que la cuenta fue eliminada, navegamos al Login
    LaunchedEffect(viewModel.cuentaEliminada) {
        if (viewModel.cuentaEliminada) {
            navController.navigate(Rutas.Acceder.ruta) {
                // Borramos todo el historial de navegación para que no pueda volver atrás
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // --- DIÁLOGO DE CONFIRMACIÓN (Alerta) ---
    if (mostrarDialogoEliminar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoEliminar = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar Cuenta?") },
            text = { Text("Esta acción es irreversible. Se borrarán todos tus datos personales y certificados asociados.") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogoEliminar = false
                        viewModel.eliminarPerfil() // Llamada al ViewModel para borrar en Backend
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Sí, Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoEliminar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    // BOTÓN PRINCIPAL DE ACCIÓN (Editar / Guardar)
                    IconButton(onClick = {
                        if (viewModel.isEditing) {
                            viewModel.guardarCambios() // Si está editando, Guarda
                        } else {
                            viewModel.toggleEdit() // Si está leyendo, Activa edición
                        }
                    }) {
                        Icon(
                            imageVector = if (viewModel.isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (viewModel.isEditing) "Guardar" else "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // BOTÓN DE CANCELAR (Solo visible si se está editando)
                    if (viewModel.isEditing) {
                        IconButton(onClick = { viewModel.toggleEdit() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancelar edición", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {

            // Si está cargando datos del backend, mostramos spinner
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ICONO DE USUARIO
                    Icon(
                        Icons.Default.Person,
                        null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = if (viewModel.isEditing) "Editando información..." else "Información Personal",
                        style = MaterialTheme.typography.titleLarge,
                        color = if (viewModel.isEditing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )

                    // --- CAMPOS DE DATOS (Conectados al ViewModel) ---

                    // 1. CAMPOS DE SOLO LECTURA (Inmutables por seguridad/lógica)
                    CampoPerfil(label = "RUT", valor = viewModel.rut, enabled = false)
                    CampoPerfil(label = "Correo Electrónico", valor = viewModel.email, enabled = false)
                    CampoPerfil(label = "Fecha de Nacimiento", valor = viewModel.fechaNacimiento, enabled = false)

                    // 2. CAMPOS EDITABLES (Se habilitan con isEditing)
                    CampoPerfil(
                        label = "Nombre Completo",
                        valor = viewModel.nombre,
                        enabled = viewModel.isEditing,
                        onValueChange = viewModel::onNombreChange
                    )

                    CampoPerfil(
                        label = "Teléfono",
                        valor = viewModel.telefono,
                        enabled = viewModel.isEditing,
                        onValueChange = viewModel::onTelefonoChange
                    )

                    CampoPerfil(
                        label = "Región",
                        valor = viewModel.region,
                        enabled = viewModel.isEditing,
                        onValueChange = viewModel::onRegionChange
                    )

                    // Género (Lo dejamos editable como texto simple para no complicar con RadioButtons aquí)
                    CampoPerfil(
                        label = "Género",
                        valor = viewModel.genero,
                        enabled = viewModel.isEditing,
                        // Si quisieras bloquearlo, cambia enabled a false
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Seguridad", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))

                    // 3. CAMBIO DE CONTRASEÑA
                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(if (viewModel.isEditing) "Nueva Contraseña (Opcional)" else "Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.isEditing, // Solo se puede escribir si se apretó el lápiz
                        visualTransformation = PasswordVisualTransformation(), // Oculta caracteres
                        placeholder = { Text("Dejar en blanco para mantener la actual") },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )

                    // MENSAJES DE FEEDBACK (Éxito o Error)
                    viewModel.mensaje?.let {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (it.contains("Error") || it.contains("No")) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = it,
                                modifier = Modifier.padding(16.dp),
                                color = if (it.contains("Error") || it.contains("No")) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- ZONA DE PELIGRO: ELIMINAR CUENTA ---
                    if (!viewModel.isEditing) { // Solo mostramos eliminar si no estamos editando, para evitar clicks accidentales
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedButton(
                            onClick = { mostrarDialogoEliminar = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                            border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.error)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.DeleteForever, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Eliminar Cuenta")
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

// COMPONENTE REUTILIZABLE PARA LOS CAMPOS DE TEXTO
@Composable
fun CampoPerfil(
    label: String,
    valor: String,
    enabled: Boolean,
    onValueChange: (String) -> Unit = {}
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            disabledContainerColor = if (!enabled) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
        )
    )
}