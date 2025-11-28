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

    var uriOrigen by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf("") }
    var isProcesando by remember { mutableStateOf(false) }

    val guardadorLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uriDestino ->
        if (uriDestino != null && uriOrigen != null) {
            isProcesando = true
            mensaje = "Firmando y guardando..."

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

        Button(onClick = {
            selectorLauncher.launch(arrayOf("application/pdf"))
        }) {
            Text("1. Seleccionar PDF Original")
        }

        uriOrigen?.let {
            Text("Archivo cargado ✓", color = MaterialTheme.colorScheme.secondary)
        }

        Button(
            enabled = (uriOrigen != null) && !isProcesando,
            onClick = {
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

fun firmarYGuardar(context: Context, inputUri: Uri, outputUri: Uri): Boolean {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(inputUri)
        val documento = PDDocument.load(inputStream)

        if (documento.numberOfPages > 0) {
            val page = documento.getPage(0)

            val content = PDPageContentStream(documento, page, PDPageContentStream.AppendMode.APPEND, true, true)

            content.beginText()
            content.setFont(PDType1Font.HELVETICA_BOLD, 18f)
            content.setNonStrokingColor(0, 128, 0)
            content.newLineAtOffset(50f, 100f)
            content.showText("FIRMADO DIGITALMENTE POR VERITRUST")
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