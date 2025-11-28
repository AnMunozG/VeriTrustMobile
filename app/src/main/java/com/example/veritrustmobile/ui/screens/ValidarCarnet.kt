package com.example.veritrustmobile.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun ValidarCarnetScreen(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel()
) {
    val context = LocalContext.current

    // Estados para las fotos
    var uriFotoFrontal by remember { mutableStateOf<Uri?>(null) }
    var uriFotoTrasera by remember { mutableStateOf<Uri?>(null) }

    var tempUriFrontal by remember { mutableStateOf<Uri?>(null) }
    var tempUriTrasera by remember { mutableStateOf<Uri?>(null) }

    // --- LAUNCHERS DE CÁMARA (Toman la foto) ---
    val cameraLauncherFrontal = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitoso ->
        if (exitoso && tempUriFrontal != null) uriFotoFrontal = tempUriFrontal
    }

    val cameraLauncherTrasera = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exitoso ->
        if (exitoso && tempUriTrasera != null) uriFotoTrasera = tempUriTrasera
    }

    // --- LAUNCHER DE PERMISOS (Pide permiso al usuario) ---
    // Variable para saber qué botón apretó (0: ninguno, 1: frontal, 2: trasera)
    var botonPresionado by remember { mutableStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // Si dio permiso, lanzamos la cámara según el botón que apretó
            if (botonPresionado == 1) {
                val uri = crearArchivoImagen(context)
                tempUriFrontal = uri
                cameraLauncherFrontal.launch(uri)
            } else if (botonPresionado == 2) {
                val uri = crearArchivoImagen(context)
                tempUriTrasera = uri
                cameraLauncherTrasera.launch(uri)
            }
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    // --- FUNCIÓN PARA VERIFICAR Y LANZAR ---
    fun checkAndLaunchCamera(tipo: Int) {
        botonPresionado = tipo
        val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            // Ya tiene permiso, lanza directo
            if (tipo == 1) {
                val uri = crearArchivoImagen(context)
                tempUriFrontal = uri
                cameraLauncherFrontal.launch(uri)
            } else {
                val uri = crearArchivoImagen(context)
                tempUriTrasera = uri
                cameraLauncherTrasera.launch(uri)
            }
        } else {
            // No tiene permiso, lo pide
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Navegación
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            if (event is RegistroViewModel.NavigationEvent.NavigateToLogin) {
                navController.navigate(Rutas.Acceder.ruta) {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Validación de Identidad",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toma una foto real de tu carnet.",
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // FOTO FRONTAL
                FotoCarnetItem(
                    texto = "Frontal",
                    uriFoto = uriFotoFrontal,
                    onTomarFoto = { checkAndLaunchCamera(1) } // 1 = Frontal
                )

                // FOTO TRASERA
                FotoCarnetItem(
                    texto = "Trasera",
                    uriFoto = uriFotoTrasera,
                    onTomarFoto = { checkAndLaunchCamera(2) } // 2 = Trasera
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (viewModel.errorRut != null) {
                Text(
                    text = viewModel.errorRut ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (viewModel.estaCargando) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { viewModel.finalizarRegistro() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = uriFotoFrontal != null && uriFotoTrasera != null
                ) {
                    Text("Finalizar Validación")
                }
            }
        }
    }
}

@Composable
fun FotoCarnetItem(texto: String, uriFoto: Uri?, onTomarFoto: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier.size(width = 150.dp, height = 100.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (uriFoto != null) {
                    AsyncImage(
                        model = uriFoto,
                        contentDescription = "Foto tomada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        imageVector = Icons.Filled.CameraAlt,
                        contentDescription = "Cámara",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onTomarFoto) {
            Text(if (uriFoto == null) texto else "Repetir")
        }
    }
}

fun crearArchivoImagen(context: Context): Uri {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.externalCacheDir
    val image = File.createTempFile(imageFileName, ".jpg", storageDir)
    return FileProvider.getUriForFile(
        context,
        "com.example.veritrustmobile.provider",
        image
    )
}