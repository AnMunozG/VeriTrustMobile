package com.example.veritrustmobile.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
// 1. Cambia la herencia a AndroidViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel para la pantalla de Acceso (Login).
 * Gestiona el estado del formulario de login y se comunica con el AuthRepository.
 */
// 2. La clase ahora hereda de AndroidViewModel y recibe 'application' en su constructor.
class AccederViewModel(application: Application) : AndroidViewModel(application) {

    // 3. El ViewModel puede crear el Repositorio porque ahora tiene el contexto.
    private val authRepository = AuthRepository(application.applicationContext)

    // --- Estado del formulario ---
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var loginError by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    // --- Eventos de Navegación ---
    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // --- Eventos de la UI ---
    fun onEmailChange(value: String) {
        email = value
        loginError = null // Limpia el error al escribir
    }

    fun onPasswordChange(value: String) {
        password = value
        loginError = null // Limpia el error al escribir
    }

    /**
     * Inicia el proceso de login.
     */
    fun onLoginClick() {
        if (email.isBlank() || password.isBlank()) {
            loginError = "El correo y la contraseña no pueden estar vacíos."
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                // La operación de base de datos se ejecuta en un hilo de fondo (IO).
                val user = withContext(Dispatchers.IO) {
                    authRepository.findUserByCredentials(email, password)
                }

                if (user != null) {
                    // Si el repositorio encuentra al usuario, emite el evento de navegación.
                    _navigationEvent.emit(NavigationEvent.NavigateToHome(user.user))
                } else {
                    // Si el repositorio devuelve null, las credenciales son incorrectas.
                    loginError = "El correo o la contraseña no coinciden."
                }
            } catch (e: Exception) {
                loginError = "Ocurrió un error inesperado. Inténtalo de nuevo."
                e.printStackTrace()
            } finally {
                isLoading = false // Desactiva el indicador de carga, sin importar el resultado.
            }
        }
    }

    sealed class NavigationEvent {
        data class NavigateToHome(val userEmail: String) : NavigationEvent()
    }

    // 4. Ya no se necesita una Factory manual, gracias a AndroidViewModel.
}
