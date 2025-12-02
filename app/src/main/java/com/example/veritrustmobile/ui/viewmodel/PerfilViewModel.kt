package com.example.veritrustmobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veritrustmobile.SessionManager
import com.example.veritrustmobile.data.RetrofitClient
import com.example.veritrustmobile.model.User
import kotlinx.coroutines.launch

class PerfilViewModel : ViewModel() {

    // --- ESTADOS DE LOS DATOS DEL USUARIO ---
    var nombre by mutableStateOf("")
    var rut by mutableStateOf("")
    var email by mutableStateOf("") // Actúa como ID
    var telefono by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var region by mutableStateOf("")
    var genero by mutableStateOf("")

    // Contraseña: Se mantiene separada. Si se deja vacía al guardar, no se cambia.
    var password by mutableStateOf("")

    // --- ESTADOS DE LA INTERFAZ ---
    var isEditing by mutableStateOf(false) // ¿Están los campos desbloqueados?
    var isLoading by mutableStateOf(false) // Spinner de carga
    var mensaje by mutableStateOf<String?>(null) // Feedback para el usuario (Toasts/Snackbars)

    // Estado crítico: Indica a la vista si la cuenta fue borrada para navegar al Login
    var cuentaEliminada by mutableStateOf(false)

    // Al iniciar el ViewModel, cargamos los datos automáticamente
    init {
        cargarDatosUsuario()
    }

    // Alternar entre modo Lectura y Edición
    fun toggleEdit() {
        if (isEditing) {
            // Si estaba editando y cancela (vuelve a tocar el botón sin guardar),
            // recargamos los datos originales para deshacer cambios no guardados.
            cargarDatosUsuario()
        }
        isEditing = !isEditing
        mensaje = null
    }

    // 1. CARGAR (READ)
    private fun cargarDatosUsuario() {
        val emailGuardado = SessionManager.getToken() // Obtenemos el email de la sesión local

        if (emailGuardado.isNullOrEmpty()) {
            mensaje = "No hay sesión activa"
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                // Llamada GET al backend
                val response = RetrofitClient.api.getUsuario(emailGuardado)

                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        // Llenamos los estados con datos reales del servidor
                        nombre = it.nombre
                        rut = it.rut
                        email = it.user
                        telefono = it.telefono
                        fechaNacimiento = it.fechaNacimiento
                        region = it.region
                        genero = it.genero
                        password = "" // Por seguridad, siempre limpiamos la contraseña visual
                    }
                } else {
                    mensaje = "Error al cargar perfil. Código: ${response.code()}"
                }
            } catch (e: Exception) {
                mensaje = "Error de conexión con el servidor"
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // 2. GUARDAR (UPDATE)
    fun guardarCambios() {
        viewModelScope.launch {
            isLoading = true
            try {
                // Preparamos el objeto Usuario con los datos editados
                // Si password está vacía, el Backend está configurado para ignorarla y mantener la anterior
                val usuarioActualizado = User(
                    user = email, // El email no se debe cambiar aquí
                    password = if (password.isNotBlank()) password else "",
                    nombre = nombre,
                    rut = rut,
                    telefono = telefono,
                    fechaNacimiento = fechaNacimiento,
                    region = region,
                    genero = genero
                )

                val response = RetrofitClient.api.actualizarUsuario(usuarioActualizado)

                if (response.isSuccessful) {
                    mensaje = "Datos actualizados correctamente"
                    isEditing = false // Volvemos a bloquear los campos (modo lectura)
                    password = "" // Limpiamos el campo de contraseña
                } else {
                    mensaje = "Error al actualizar. Verifique los datos."
                }
            } catch (e: Exception) {
                mensaje = "Error al conectar con el servidor"
            } finally {
                isLoading = false
            }
        }
    }

    // 3. ELIMINAR (DELETE)
    fun eliminarPerfil() {
        val emailGuardado = SessionManager.getToken() ?: return

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.eliminarUsuario(emailGuardado)

                // El backend devuelve 204 No Content o 200 OK si tuvo éxito
                if (response.isSuccessful) {
                    SessionManager.clearSession() // Borramos sesión local en el teléfono
                    cuentaEliminada = true // Disparamos la navegación hacia el Login
                } else {
                    mensaje = "No se pudo eliminar la cuenta. Intente más tarde."
                }
            } catch (e: Exception) {
                mensaje = "Error de conexión"
            } finally {
                isLoading = false
            }
        }
    }

    // --- SETTERS PARA LA UI (Data Binding) ---
    fun onNombreChange(v: String) { nombre = v }
    fun onTelefonoChange(v: String) { telefono = v }
    fun onRegionChange(v: String) { region = v }
    fun onPasswordChange(v: String) { password = v }
}