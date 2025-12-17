package com.example.veritrustmobile.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.veritrustmobile.navigation.Rutas
import com.example.veritrustmobile.ui.theme.VeriTrustMobileTheme
import com.example.veritrustmobile.ui.viewmodel.AccederViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Acceder(
    navController: NavController,
    viewModel: AccederViewModel = viewModel()
) {
    val context = LocalContext.current

    //PROBAR DESPUES SI VIBRA O NO
    LaunchedEffect(viewModel.loginError) {
        if (viewModel.loginError != null) {
            vibrarCelular(context)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AccederViewModel.NavigationEvent.NavigateToHome -> {
                    navController.navigate(Rutas.Servicios.ruta) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Correo Electrónico") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Icono de correo",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            isError = viewModel.loginError != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = "Icono de contraseña",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            isError = viewModel.loginError != null
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.onLoginClick() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 60.dp)
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 2.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 3.dp
                )
            } else {
                Text(
                    text = "Ingresar",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }

        viewModel.loginError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { navController.navigate(Rutas.RecuperarContrasena.ruta) },
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.padding(bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿No tienes una cuenta? ", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Regístrate",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate(Rutas.Registro.ruta) },
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}

private fun vibrarCelular(context: Context) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(500)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAcceder() {
    VeriTrustMobileTheme {
        Acceder(navController = rememberNavController())
    }
}