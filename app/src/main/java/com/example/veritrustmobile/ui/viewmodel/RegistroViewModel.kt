package com.example.veritrustmobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeParseException


class RegistroViewModel : ViewModel() {

    var rut by mutableStateOf("")
        private set
    var nombre by mutableStateOf("")
        private set
    var fechaNacimiento by mutableStateOf("") // Formato "YYYY-MM-DD"
        private set
    var telefono by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var confirmarEmail by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmarContrasena by mutableStateOf("")
        private set
    var terminosAceptados by mutableStateOf(false)
        private set

    var errorRut by mutableStateOf<String?>(null)
        private set
    var errorNombre by mutableStateOf<String?>(null)
        private set
    var errorFechaNacimiento by mutableStateOf<String?>(null)
        private set
    var errorTelefono by mutableStateOf<String?>(null)
        private set
    var errorEmail by mutableStateOf<String?>(null)
        private set
    var errorConfirmarEmail by mutableStateOf<String?>(null)
        private set
    var errorContrasena by mutableStateOf<String?>(null)
        private set
    var errorConfirmarContrasena by mutableStateOf<String?>(null)
        private set
    var errorTerminos by mutableStateOf<String?>(null)
        private set

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onRutChange(value: String) {
        rut = value
        errorRut = null
    }

    fun onNombreChange(value: String) {
        nombre = value
        errorNombre = null
    }
    fun onFechaNacimientoChange(value: String) {
        fechaNacimiento = value
        errorFechaNacimiento = null
    }

    fun onTelefonoChange(value: String) {
        telefono = value
        errorTelefono = null
    }

    fun onEmailChange(value: String) {
        email = value
        errorEmail = null
    }

    fun onConfirmarEmailChange(value: String) {
        confirmarEmail = value
        errorConfirmarEmail = null
    }

    fun onContrasenaChange(value: String) {
        contrasena = value
        errorContrasena = null
    }

    fun onConfirmarContrasenaChange(value: String) {
        confirmarContrasena = value
        errorConfirmarContrasena = null
    }

    fun onTerminosChange(value: Boolean) {
        terminosAceptados = value
        errorTerminos = null
    }

    fun onRegistroSubmit() {
        if (validarFormulario()) {
            viewModelScope.launch {
                _navigationEvent.emit(NavigationEvent.NavigateToLogin)
            }
        }
    }
    private fun validarFormulario(): Boolean {
        errorRut = if (validarRutChileno(rut)) null else "RUT inválido"
        errorNombre = if (nombre.length >= 3) null else "Ingresa un nombre válido"
        errorFechaNacimiento = when {
            fechaNacimiento.isBlank() -> "Ingresa tu fecha de nacimiento"
            !esMayorDeEdad(fechaNacimiento) -> "Debes ser mayor de 18 años"
            else -> null
        }
        errorTelefono = if (telefono.replace(Regex("[\\s+]"), "").matches(Regex("^\\d{7,15}$"))) null else "Número de teléfono inválido"
        errorEmail = if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else "Correo inválido"
        errorConfirmarEmail = if (email == confirmarEmail) null else "Los correos no coinciden"
        errorContrasena = if (contrasena.length >= 6) null else "La contraseña debe tener al menos 6 caracteres"
        errorConfirmarContrasena = if (contrasena == confirmarContrasena) null else "Las contraseñas no coinciden"
        errorTerminos = if (terminosAceptados) null else "Debes aceptar los términos y condiciones"

        // Retorna true solo si todos los campos de error son nulos.
        return listOfNotNull(errorRut, errorNombre, errorFechaNacimiento, errorTelefono, errorEmail, errorConfirmarEmail, errorContrasena, errorConfirmarContrasena, errorTerminos).isEmpty()
    }

    private fun esMayorDeEdad(fechaNacimientoStr: String): Boolean {
        return try {
            val fechaNac = LocalDate.parse(fechaNacimientoStr) // Espera formato YYYY-MM-DD
            val hoy = LocalDate.now()
            Period.between(fechaNac, hoy).years >= 18
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun validarRutChileno(rut: String): Boolean {
        val rutLimpio = rut.replace(Regex("[.-]"), "")
        if (rutLimpio.length < 2) return false

        val cuerpo = rutLimpio.substring(0, rutLimpio.length - 1)
        val dv = rutLimpio.last().uppercaseChar()

        if (!cuerpo.matches(Regex("^\\d+$"))) return false

        var suma = 0
        var multiplo = 2
        for (i in cuerpo.length - 1 downTo 0) {
            suma += cuerpo[i].toString().toInt() * multiplo
            multiplo = if (multiplo == 7) 2 else multiplo + 1
        }

        val dvEsperado = when (val resto = 11 - (suma % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> resto.toString().first()
        }

        return dv == dvEsperado
    }

    sealed class NavigationEvent {
        object NavigateToLogin : NavigationEvent()
    }
}
