package com.example.veritrustmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // 1. Inyecta el ViewModel. Compose se encargará de su ciclo de vida.
    viewModel: AccederViewModel = viewModel()
) {
    // 2. Escucha los eventos de navegación que vienen del ViewModel.
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is AccederViewModel.NavigationEvent.NavigateToHome -> {
                    // Navega a la pantalla de servicios cuando el login es exitoso.
                    navController.navigate(Rutas.Servicios.ruta) {
                        // Limpia el historial para que el usuario no pueda volver al login.
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            "Inicio de Sesión",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(25.dp))

        // 3. Los campos de texto ahora leen el estado desde el ViewModel
        // y le notifican los cambios.
        TextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Correo Electrónico") },
            leadingIcon = {
                Icon(Icons.Rounded.AccountCircle, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            isError = viewModel.loginError != null // El error de login general afecta a ambos campos.
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Rounded.Lock, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            isError = viewModel.loginError != null
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Muestra un indicador de carga si el ViewModel está ocupado.
        if (viewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp))
        } else {
            // 4. El botón ahora solo notifica al ViewModel la intención de iniciar sesión.
            Button(
                onClick = { viewModel.onLoginClick() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = "Ingresar",
                    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                )
            }
        }

        // 5. El mensaje de error ahora se lee directamente del ViewModel.
        viewModel.loginError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el contenido de abajo hacia el final

        // =========== SECCIÓN INFERIOR ===========
        Text(
            text = "Olvidaste la contraseña?",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { /* TODO: Lógica de olvidar contraseña */ },
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 32.dp)
        ) {
            Text("¿No tienes una cuenta? ", fontSize = 16.sp)
            Text(
                text = "Regístrate",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { navController.navigate(Rutas.Registro.ruta) },
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAcceder() {
    VeriTrustMobileTheme {
        Acceder(navController = rememberNavController())
    }
}
