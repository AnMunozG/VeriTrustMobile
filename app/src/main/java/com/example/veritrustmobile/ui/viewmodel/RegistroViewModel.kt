package com.example.veritrustmobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class RegistroViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    // --- ESTADOS (VARIABLES) ---
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

    // --- AQUÍ ESTABAN FALTANDO ESTAS VARIABLES ---
    var region by mutableStateOf(""); private set
    var genero by mutableStateOf(""); private set
    // ---------------------------------------------

    // --- ERRORES ---
    var errorRut by mutableStateOf<String?>(null); private set
    var errorNombre by mutableStateOf<String?>(null); private set
    var errorFechaNacimiento by mutableStateOf<String?>(null); private set
    var errorTelefono by mutableStateOf<String?>(null); private set
    var errorEmail by mutableStateOf<String?>(null); private set
    var errorConfirmarEmail by mutableStateOf<String?>(null); private set
    var errorContrasena by mutableStateOf<String?>(null); private set
    var errorConfirmarContrasena by mutableStateOf<String?>(null); private set
    var errorTerminos by mutableStateOf<String?>(null); private set

    // --- Y ESTAS VARIABLES DE ERROR TAMBIÉN FALTABAN ---
    var errorRegion by mutableStateOf<String?>(null); private set
    var errorGenero by mutableStateOf<String?>(null); private set
    // ---------------------------------------------------

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    // --- SETTERS (Funciones para cambiar el valor) ---
    fun onRutChange(value: String) { rut = value; errorRut = null }
    fun onNombreChange(value: String) { nombre = value; errorNombre = null }
    fun onFechaNacimientoChange(value: String) { fechaNacimiento = value; errorFechaNacimiento = null }
    fun onTelefonoChange(value: String) { telefono = value; errorTelefono = null }
    fun onEmailChange(value: String) { email = value; errorEmail = null }
    fun onConfirmarEmailChange(value: String) { confirmarEmail = value; errorConfirmarEmail = null }
    fun onContrasenaChange(value: String) { contrasena = value; errorContrasena = null }
    fun onConfirmarContrasenaChange(value: String) { confirmarContrasena = value; errorConfirmarContrasena = null }
    fun onTerminosChange(value: Boolean) { terminosAceptados = value; errorTerminos = null }

    // --- Y ESTAS FUNCIONES TAMBIÉN ---
    fun onRegionChange(value: String) { region = value; errorRegion = null }
    fun onGeneroChange(value: String) { genero = value; errorGenero = null }
    // ---------------------------------

    fun onRegistroSubmit() {
        if (validarFormulario()) {
            viewModelScope.launch {
                _navigationEvent.emit(NavigationEvent.NavigateToValidarCarnet)
            }
        }
    }

    fun finalizarRegistro() {
        viewModelScope.launch {
            estaCargando = true
            try {
                // AQUÍ USAMOS LAS VARIABLES QUE ANTES DABAN ERROR
                val registroExitoso = authRepository.registrarUsuario(
                    rut = rut,
                    nombre = nombre,
                    fechaNacimiento = fechaNacimiento,
                    telefono = telefono,
                    email = email,
                    contrasena = contrasena,
                    region = region, // Ahora sí existen
                    genero = genero  // Ahora sí existen
                )

                if (registroExitoso) {
                    _navigationEvent.emit(NavigationEvent.NavigateToLogin)
                } else {
                    errorRut = "Error al registrar. Inténtalo nuevamente."
                    errorEmail = "El usuario ya podría existir."
                }
            } catch (e: Exception) {
                errorRut = "Error de conexión con el servidor."
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
            !esMayorDeEdad(fechaNacimiento) -> "Debes ser mayor de 18 años"
            else -> null
        }

        errorTelefono = if (telefono.replace(Regex("[\\s+]"), "").matches(Regex("^\\d{7,15}$"))) null else "Teléfono inválido"

        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
        errorEmail = if (email.matches(emailRegex)) null else "Correo inválido"
        errorConfirmarEmail = if (email == confirmarEmail) null else "Los correos no coinciden"

        errorContrasena = if (contrasena.length >= 6) null else "Mínimo 6 caracteres"
        errorConfirmarContrasena = if (contrasena == confirmarContrasena) null else "Las contraseñas no coinciden"

        errorTerminos = if (terminosAceptados) null else "Debes aceptar los términos"

        // Validaciones nuevas
        errorRegion = if (region.isNotBlank()) null else "Selecciona una región"
        errorGenero = if (genero.isNotBlank()) null else "Selecciona un género"

        return listOfNotNull(
            errorRut, errorNombre, errorFechaNacimiento, errorTelefono,
            errorEmail, errorConfirmarEmail, errorContrasena, errorConfirmarContrasena,
            errorTerminos, errorRegion, errorGenero
        ).isEmpty()
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