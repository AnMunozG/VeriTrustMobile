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


class AccederViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

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
                val user = withContext(Dispatchers.IO) {
                    authRepository.findUserByCredentials(email, password)
                }

                if (user != null) {
                    _navigationEvent.emit(NavigationEvent.NavigateToHome(user.user))
                } else {
                    loginError = "El correo o la contraseña no coinciden."
                }
            } catch (e: Exception) {
                loginError = "Ocurrió un error inesperado. Inténtalo de nuevo."
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    sealed class NavigationEvent {
        data class NavigateToHome(val userEmail: String) : NavigationEvent()
    }

}
