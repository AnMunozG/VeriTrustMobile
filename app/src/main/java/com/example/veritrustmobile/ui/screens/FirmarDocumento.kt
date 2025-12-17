package com.example.veritrustmobile.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController // Import necesario
import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.model.Documento
import com.example.veritrustmobile.model.User
import com.example.veritrustmobile.navigation.Rutas
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.time.Instant

@Composable
fun FirmarDocumentoScreen(navController: NavController) { // Ahora recibe NavController
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var uriOrigen by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf("") }
    var isProcesando by remember { mutableStateOf(false) }

    var firmaExitosa by remember { mutableStateOf(false) }

    val guardadorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uriDestino ->
        if (uriDestino != null && uriOrigen != null) {
            isProcesando = true
            mensaje = "Firmando y enviando al servidor..."

            scope.launch(Dispatchers.IO) {
                val exitoLocal = firmarYGuardar(context, uriOrigen!!, uriDestino)

                if (exitoLocal) {
                    try {
                        val emailUsuario = SessionManager.getToken() ?: "anonimo"
                        val nuevoDoc = Documento(
                            nombreArchivo = "Documento_Firmado.pdf",
                            direccionArchivo = uriDestino.toString(),
                            fechaFirmado = Instant.now().toString(),
                            usuario = User(user = emailUsuario, password = "")
                        )

                        val response = RetrofitClient.api.guardarDocumento(nuevoDoc)

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                mensaje = "¡Proceso completado con éxito!"
                                firmaExitosa = true // Activamos el botón de volver
                                Toast.makeText(context, "Guardado OK", Toast.LENGTH_LONG).show()
                            } else {
                                mensaje = "Firmado local, error en nube: ${response.code()}"
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) { mensaje = "Error de conexión al guardar respaldo" }
                    }
                } else {
                    withContext(Dispatchers.Main) { mensaje = "Error al firmar PDF local." }
                }
                isProcesando = false
            }
        }
    }

    val selectorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            uriOrigen = uri
            mensaje = "Archivo cargado. Listo para firmar."
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Firma Digital", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)

        if (firmaExitosa) {
            Text("¡Documento firmado correctamente!", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Button(
                onClick = {
                    navController.navigate(Rutas.Servicios.crearRuta(false)) {
                        popUpTo(Rutas.Inicio.ruta) { inclusive = false }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Volver al Inicio")
            }
        } else {
            Text("1. Selecciona un PDF de tu celular.", textAlign = TextAlign.Center)
            Button(onClick = { selectorLauncher.launch(arrayOf("application/pdf")) }) {
                Text("Cargar PDF")
            }

            if (uriOrigen != null) {
                Text("Archivo cargado ✓", color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(8.dp))

                Text("2. Firma y guarda el documento.", textAlign = TextAlign.Center)
                Button(
                    enabled = !isProcesando,
                    onClick = {
                        val nombreNuevo = "VeriTrust_Firmado_${System.currentTimeMillis()}.pdf"
                        guardadorLauncher.launch(nombreNuevo)
                    }
                ) {
                    if (isProcesando) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Firmar y Guardar")
                    }
                }
            }
        }

        if (mensaje.isNotBlank()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                Text(text = mensaje, modifier = Modifier.padding(16.dp), textAlign = TextAlign.Center)
            }
        }
    }
}

fun firmarYGuardar(context: Context, inputUri: Uri, outputUri: Uri): Boolean {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(inputUri)
        val documento = PDDocument.load(inputStream)
        if (documento.numberOfPages > 0) {
            val page = documento.getPage(0)
            val content = PDPageContentStream(documento, page, PDPageContentStream.AppendMode.APPEND, true, true)
            content.beginText()
            content.setFont(PDType1Font.HELVETICA_BOLD, 14f)
            content.setNonStrokingColor(0, 100, 0)
            content.newLineAtOffset(50f, 50f)
            content.showText("FIRMADO DIGITALMENTE POR VERITRUST - " + Instant.now().toString())
            content.endText()
            content.close()
        }
        val outputStream = context.contentResolver.openOutputStream(outputUri)
        if (outputStream != null) {
            documento.save(outputStream)
            outputStream.close()
        }
        documento.close()
        inputStream?.close()
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}