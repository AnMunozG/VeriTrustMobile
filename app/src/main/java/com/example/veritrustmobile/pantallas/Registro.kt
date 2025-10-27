package com.example.veritrustmobile.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.veritrustmobile.rutas.Rutas

/**
 * Composable reutilizable para un campo de texto estándar del formulario.
 *
 * @param valor El valor actual del campo de texto.
 * @param alCambiarValor Lambda que se invoca cuando el valor cambia.
 * @param etiqueta El texto que se muestra como label del campo.
 * @param esContrasena Indica si el campo debe ocultar el texto (para contraseñas).
 * @param tipoDeTeclado Define el tipo de teclado a mostrar (ej. texto, email, número).
 */
@Composable
private fun CampoDeTextoFormulario(
    valor: String,
    alCambiarValor: (String) -> Unit,
    etiqueta: String,
    esContrasena: Boolean = false,
    tipoDeTeclado: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = valor,
        onValueChange = alCambiarValor,
        label = { Text(etiqueta) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = tipoDeTeclado)
    )
}
@Composable
fun Registro(navController: NavHostController) {

    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear una cuenta",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoDeTextoFormulario(
            valor = rut,
            alCambiarValor = { rut = it },
            etiqueta = "RUT"
        )
        Spacer(modifier = Modifier.height(8.dp))

        CampoDeTextoFormulario(
            valor = nombre,
            alCambiarValor = { nombre = it },
            etiqueta = "Nombre completo"
        )
        Spacer(modifier = Modifier.height(8.dp))

        CampoDeTextoFormulario(
            valor = email,
            alCambiarValor = { email = it },
            etiqueta = "Email",
            tipoDeTeclado = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(8.dp))

        CampoDeTextoFormulario(
            valor = contrasena,
            alCambiarValor = { contrasena = it },
            etiqueta = "Contraseña",
            esContrasena = true,
            tipoDeTeclado = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(8.dp))

        CampoDeTextoFormulario(
            valor = confirmarContrasena,
            alCambiarValor = { confirmarContrasena = it },
            etiqueta = "Confirmar contraseña",
            esContrasena = true,
            tipoDeTeclado = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { // Faltan las comprobaciones y asi XD no se como hacerlas todavia
                navController.navigate(Rutas.Acceder.ruta)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}
