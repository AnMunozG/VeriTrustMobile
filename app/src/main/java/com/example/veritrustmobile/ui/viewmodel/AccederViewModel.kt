package com.example.veritrustmobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.UnknownHostException

class AccederViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var loginError by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEmailChange(value: String) {
        email = value
        loginError = null
    }

    fun onPasswordChange(value: String) {
        password = value
        loginError = null
    }

    fun onLoginClick() {
        if (email.isBlank() || password.isBlank()) {
            loginError = "El correo y la contraseña no pueden estar vacíos."
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val user = authRepository.login(email, password)

                if (user != null) {
                    SessionManager.saveToken(user.user)
                    SessionManager.saveRol(user.rol) // ⭐ GUARDAR ROL

                    _navigationEvent.emit(NavigationEvent.NavigateToHome(user.user))
                } else {
                    loginError = "El correo o la contraseña no coinciden."
                }
            } catch (e: UnknownHostException) {
                loginError = "No hay conexión a internet. Revisa tu red."
            } catch (e: IOException) {
                loginError = "Error de red. Inténtalo de nuevo."
            } catch (e: Exception) {
                loginError = "Ocurrió un error inesperado. Inténtalo más tarde."
            } finally {
                isLoading = false
            }
        }
    }

    sealed class NavigationEvent {
        data class NavigateToHome(val userEmail: String) : NavigationEvent()
    }
}