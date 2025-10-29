package com.example.veritrustmobile.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application.applicationContext)

    // --- Estados del formulario ---
    var rut by mutableStateOf(""); private set
    var nombre by mutableStateOf(""); private set
    var fechaNacimiento by mutableStateOf(""); private set
    var telefono by mutableStateOf(""); private set
    var email by mutableStateOf(""); private set
    var confirmarEmail by mutableStateOf(""); private set
    var contrasena by mutableStateOf(""); private set
    var confirmarContrasena by mutableStateOf(""); private set
    var terminosAceptados by mutableStateOf(false); private set
    var estaCargando by mutableStateOf(false); private set

    // --- Estados de error ---
    var errorRut by mutableStateOf<String?>(null); private set
    var errorNombre by mutableStateOf<String?>(null); private set
    var errorFechaNacimiento by mutableStateOf<String?>(null); private set
    var errorTelefono by mutableStateOf<String?>(null); private set
    var errorEmail by mutableStateOf<String?>(null); private set
    var errorConfirmarEmail by mutableStateOf<String?>(null); private set
    var errorContrasena by mutableStateOf<String?>(null); private set
    var errorConfirmarContrasena by mutableStateOf<String?>(null); private set
    var errorTerminos by mutableStateOf<String?>(null); private set

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // --- Funciones on...Change (para actualizar el estado desde la UI) ---
    fun onRutChange(value: String) { rut = value; errorRut = null }
    fun onNombreChange(value: String) { nombre = value; errorNombre = null }
    fun onFechaNacimientoChange(value: String) { fechaNacimiento = value; errorFechaNacimiento = null }
    fun onTelefonoChange(value: String) { telefono = value; errorTelefono = null }
    fun onEmailChange(value: String) { email = value; errorEmail = null }
    fun onConfirmarEmailChange(value: String) { confirmarEmail = value; errorConfirmarEmail = null }
    fun onContrasenaChange(value: String) { contrasena = value; errorContrasena = null }
    fun onConfirmarContrasenaChange(value: String) { confirmarContrasena = value; errorConfirmarContrasena = null }
    fun onTerminosChange(value: Boolean) { terminosAceptados = value; errorTerminos = null }

    /**
     * Inicia el proceso de registro. Ahora valida primero y luego emite el evento.
     */
    fun onRegistroSubmit() {
        if (validarFormulario()) {
            viewModelScope.launch {
                _navigationEvent.emit(NavigationEvent.NavigateToValidarCarnet)
            }
        }
    }

    /**
     * Llama al repositorio para guardar el usuario en la base de datos.
     */
    fun finalizarRegistro() {
        viewModelScope.launch {
            estaCargando = true
            try {
                val registroExitoso = withContext(Dispatchers.IO) {
                    authRepository.registrarUsuario(
                        rut = rut,
                        nombre = nombre,
                        fechaNacimiento = fechaNacimiento,
                        telefono = telefono,
                        email = email,
                        contrasena = contrasena
                    )
                }
                if (registroExitoso) {
                    _navigationEvent.emit(NavigationEvent.NavigateToLogin)
                } else {
                    errorRut = "El RUT o el correo ya están registrados."
                    errorEmail = "El RUT o el correo ya están registrados."
                }
            } finally {
                estaCargando = false
            }
        }
    }

    internal fun validarFormulario(): Boolean {
        errorRut = if (validarRutChileno(rut)) null else "RUT inválido"
        errorNombre = if (nombre.length >= 3) null else "Ingresa un nombre válido"
        errorFechaNacimiento = when {
            fechaNacimiento.isBlank() -> "Ingresa tu fecha de nacimiento"
            !esMayorDeEdad(fechaNacimiento) -> "Formato de fecha inválido o eres menor de 18 años"
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
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return try {
            val fechaNac = LocalDate.parse(fechaNacimientoStr, formatter)
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
        object NavigateToValidarCarnet : NavigationEvent()
    }
}
