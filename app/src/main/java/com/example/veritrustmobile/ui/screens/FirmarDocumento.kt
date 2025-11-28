package com.example.veritrustmobile.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.font.PDType1Font
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@Composable
fun FirmarDocumentoScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Variables de estado
    var uriOrigen by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf("") }
    var isProcesando by remember { mutableStateOf(false) }

    // 1. LAUNCHER PARA GUARDAR EL ARCHIVO FIRMADO (Esto abre el menú "Guardar como" del celular)
    val guardadorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uriDestino ->
        if (uriDestino != null && uriOrigen != null) {
            isProcesando = true
            mensaje = "Firmando y guardando..."

            // Ejecutamos la firma en segundo plano
            scope.launch(Dispatchers.IO) {
                val exito = firmarYGuardar(context, uriOrigen!!, uriDestino)

                withContext(Dispatchers.Main) {
                    isProcesando = false
                    if (exito) {
                        mensaje = "¡Documento firmado exitosamente!"
                        Toast.makeText(context, "Guardado correctamente", Toast.LENGTH_LONG).show()
                    } else {
                        mensaje = "Error al firmar el PDF. Verifica que sea válido."
                    }
                }
            }
        }
    }

    // 2. LAUNCHER PARA SELECCIONAR EL ARCHIVO ORIGINAL
    val selectorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            uriOrigen = uri
            mensaje = "Archivo seleccionado. Pulsa 'Firmar' para guardarlo."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Firmar Documento",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            "Selecciona un PDF y elige dónde guardar la versión firmada digitalmente.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        // Botón 1: Seleccionar
        Button(onClick = {
            // Filtramos solo PDFs para evitar errores
            selectorLauncher.launch(arrayOf("application/pdf"))
        }) {
            Text("1. Seleccionar PDF Original")
        }

        // Feedback visual de selección
        uriOrigen?.let {
            Text("Archivo cargado ✓", color = MaterialTheme.colorScheme.secondary)
        }

        // Botón 2: Firmar y Guardar
        Button(
            enabled = (uriOrigen != null) && !isProcesando,
            onClick = {
                // Sugerimos un nombre para el nuevo archivo
                val nombreNuevo = "VeriTrust_Firmado_${System.currentTimeMillis()}.pdf"
                guardadorLauncher.launch(nombreNuevo)
            }
        ) {
            if (isProcesando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("2. Firmar y Guardar")
            }
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                color = if(mensaje.contains("Error")) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- FUNCIÓN DE LÓGICA PURA PARA FIRMAR EL PDF ---
fun firmarYGuardar(context: Context, inputUri: Uri, outputUri: Uri): Boolean {
    return try {
        // A. Abrimos el PDF original
        val inputStream: InputStream? = context.contentResolver.openInputStream(inputUri)
        val documento = PDDocument.load(inputStream)

        // B. Firmamos en la primera página
        // Si el documento tiene páginas, tomamos la primera
        if (documento.numberOfPages > 0) {
            val page = documento.getPage(0)

            // "AppendMode.APPEND" es clave para escribir SOBRE lo que ya existe
            val content = PDPageContentStream(documento, page, PDPageContentStream.AppendMode.APPEND, true, true)

            content.beginText()
            content.setFont(PDType1Font.HELVETICA_BOLD, 18f)
            content.setNonStrokingColor(0, 128, 0) // Color verde
            content.newLineAtOffset(50f, 100f) // Coordenadas (X, Y) desde abajo a la izquierda
            content.showText("FIRMADO DIGITALMENTE POR VERITRUST")
            content.endText()
            content.close()
        }

        // C. Guardamos en el nuevo archivo (URI de destino)
        val outputStream = context.contentResolver.openOutputStream(outputUri)
        if (outputStream != null) {
            documento.save(outputStream)
            outputStream.close()
        }

        documento.close()
        inputStream?.close()
        true // Todo salió bien

    } catch (e: Exception) {
        e.printStackTrace()
        false // Hubo error
    }
}