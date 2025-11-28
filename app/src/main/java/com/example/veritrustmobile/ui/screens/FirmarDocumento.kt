package com.example.veritrustmobile.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.veritrustmobile.ui.components.simularFirmaDocumento

@Composable
fun FirmarDocumentoScreen() {

    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var mensaje by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            selectedUri = uri
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "Firmar documento con VeriTrust",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Button(onClick = {
            launcher.launch(arrayOf("application/pdf", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        }) {
            Text("Seleccionar Archivo")
        }

        selectedUri?.let { uri ->
            Text("Archivo seleccionado: $uri")

            Button(
                onClick = {
                    val outUri = simularFirmaDocumento(context, uri)
                    mensaje = if (outUri != null)
                        "Documento firmado guardado en Descargas."
                    else
                        "Error al firmar documento."
                }
            ) {
                Text("Firmar Documento")
            }
        }

        if (mensaje.isNotBlank()) {
            Text(mensaje, color = MaterialTheme.colorScheme.secondary)
        }
    }
}
