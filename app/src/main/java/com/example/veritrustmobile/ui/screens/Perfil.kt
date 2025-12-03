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

    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.cuentaEliminada) {
        if (viewModel.cuentaEliminada) {
            navController.navigate(Rutas.Acceder.ruta) {
                // Borramos todo el historial de navegación para que no pueda volver atrás
                popUpTo(0) { inclusive = true }
            }
        }
    }

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
                        viewModel.eliminarPerfil()
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

                    IconButton(onClick = {
                        if (viewModel.isEditing) {
                            viewModel.guardarCambios()
                        } else {
                            viewModel.toggleEdit()
                        }
                    }) {
                        Icon(
                            imageVector = if (viewModel.isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (viewModel.isEditing) "Guardar" else "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }


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




                    CampoPerfil(label = "RUT", valor = viewModel.rut, enabled = false)
                    CampoPerfil(label = "Correo Electrónico", valor = viewModel.email, enabled = false)
                    CampoPerfil(label = "Fecha de Nacimiento", valor = viewModel.fechaNacimiento, enabled = false)


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

                    CampoPerfil(
                        label = "Género",
                        valor = viewModel.genero,
                        enabled = viewModel.isEditing,
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Seguridad", style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))

                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text(if (viewModel.isEditing) "Nueva Contraseña (Opcional)" else "Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = viewModel.isEditing,
                        visualTransformation = PasswordVisualTransformation(),
                        placeholder = { Text("Dejar en blanco para mantener la actual") },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    )


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
                    if (!viewModel.isEditing) {
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