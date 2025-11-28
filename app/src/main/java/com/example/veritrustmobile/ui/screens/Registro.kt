package com.example.veritrustmobile.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    navController: NavHostController,
    viewModel: RegistroViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is RegistroViewModel.NavigationEvent.NavigateToLogin -> {
                    navController.navigate(Rutas.Acceder.ruta) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
                is RegistroViewModel.NavigationEvent.NavigateToValidarCarnet -> {
                    navController.navigate(Rutas.ValidarCarnet.ruta)
                }
            }
        }
    }

    RegistroContent(viewModel = viewModel)
}

@Composable
fun RegistroContent(viewModel: RegistroViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crea tu cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))


        CampoDeTextoFormulario(
            valor = viewModel.rut,
            onValueChange = viewModel::onRutChange,
            etiqueta = "RUT/RUN (Ej: 12345678-9)",
            error = viewModel.errorRut
        )
        CampoDeTextoFormulario(
            valor = viewModel.nombre,
            onValueChange = viewModel::onNombreChange,
            etiqueta = "Nombre completo",
            error = viewModel.errorNombre
        )
        CampoDeTextoFormulario(
            valor = viewModel.fechaNacimiento,
            onValueChange = viewModel::onFechaNacimientoChange,
            etiqueta = "Fecha de Nacimiento (DD/MM/AAAA)",
            error = viewModel.errorFechaNacimiento,
            tipoDeTeclado = KeyboardType.Number
        )
        CampoDeTextoFormulario(
            valor = viewModel.telefono,
            onValueChange = viewModel::onTelefonoChange,
            etiqueta = "Teléfono",
            error = viewModel.errorTelefono,
            tipoDeTeclado = KeyboardType.Phone
        )
        CampoDeTextoFormulario(
            valor = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            etiqueta = "Correo electrónico",
            error = viewModel.errorEmail,
            tipoDeTeclado = KeyboardType.Email
        )
        CampoDeTextoFormulario(
            valor = viewModel.confirmarEmail,
            onValueChange = viewModel::onConfirmarEmailChange,
            etiqueta = "Confirma tu correo electrónico",
            error = viewModel.errorConfirmarEmail,
            tipoDeTeclado = KeyboardType.Email
        )
        CampoDeTextoFormulario(
            valor = viewModel.contrasena,
            onValueChange = viewModel::onContrasenaChange,
            etiqueta = "Contraseña",
            error = viewModel.errorContrasena,
            esContrasena = true,
            tipoDeTeclado = KeyboardType.Password
        )
        CampoDeTextoFormulario(
            valor = viewModel.confirmarContrasena,
            onValueChange = viewModel::onConfirmarContrasenaChange,
            etiqueta = "Confirma tu contraseña",
            error = viewModel.errorConfirmarContrasena,
            esContrasena = true,
            tipoDeTeclado = KeyboardType.Password
        )


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable { viewModel.onTerminosChange(!viewModel.terminosAceptados) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = viewModel.terminosAceptados,
                onCheckedChange = { viewModel.onTerminosChange(it) }
            )
            Text(
                text = "Acepto los términos y condiciones",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        viewModel.errorTerminos?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = viewModel::onRegistroSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp) // Botón redondeado
        ) {
            Text("Continuar")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun CampoDeTextoFormulario(
    valor: String,
    onValueChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    esContrasena: Boolean = false,
    tipoDeTeclado: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier.padding(bottom = 8.dp)) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            label = { Text(etiqueta) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (esContrasena) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = tipoDeTeclado),
            isError = error != null,
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegistroScreenPreview() {
    VeriTrustMobileTheme {
        Surface {
            RegistroScreen(rememberNavController())
        }
    }
}
