package com.example.veritrustmobile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme

@Composable
fun Nosotros() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Sobre Nosotros",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "SOLUCIONES INTEGRALES PARA EMPRESAS Y USUARIOS",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "En VeriTrust, nos dedicamos a ofrecer servicios de identidad digital de alta confiabilidad. Nuestra experiencia en certificación digital, firma electrónica y verificación de identidad permite a organizaciones y personas realizar sus gestiones en línea de manera segura, eficiente y conforme a los estándares legales y tecnológicos más exigentes.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Desde certificados digitales hasta firmas electrónicas reconocidas por el SII, proporcionamos las herramientas necesarias para que tu empresa opere en el mundo digital con total tranquilidad y respaldo legal.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun NosotrosPreview() {
    VeriTrustMobileTheme {
        Surface {
            Nosotros()
        }
    }
}
