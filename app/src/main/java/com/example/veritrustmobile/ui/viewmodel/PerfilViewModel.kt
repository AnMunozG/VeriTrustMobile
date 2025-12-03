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

    var nombre by mutableStateOf("")
    var rut by mutableStateOf("")
    var email by mutableStateOf("")
    var telefono by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var region by mutableStateOf("")
    var genero by mutableStateOf("")

    var password by mutableStateOf("")

    var isEditing by mutableStateOf(false)
    var isLoading by mutableStateOf(false)
    var mensaje by mutableStateOf<String?>(null)

    // Estado crítico: Indica a la vista si la cuenta fue borrada para navegar al Login
    var cuentaEliminada by mutableStateOf(false)

    init {
        cargarDatosUsuario()
    }

    fun toggleEdit() {
        if (isEditing) {
            // Si estaba editando y cancela (vuelve a tocar el botón sin guardar),
            cargarDatosUsuario()
        }
        isEditing = !isEditing
        mensaje = null
    }

    // 1. CARGAR (READ)
    private fun cargarDatosUsuario() {
        val emailGuardado = SessionManager.getToken()

        if (emailGuardado.isNullOrEmpty()) {
            mensaje = "No hay sesión activa"
            return
        }

        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.api.getUsuario(emailGuardado)

                if (response.isSuccessful) {
                    val user = response.body()
                    user?.let {
                        nombre = it.nombre
                        rut = it.rut
                        email = it.user
                        telefono = it.telefono
                        fechaNacimiento = it.fechaNacimiento
                        region = it.region
                        genero = it.genero
                        password = ""
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
                    isEditing = false
                    password = ""
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

                if (response.isSuccessful) {
                    SessionManager.clearSession()
                    cuentaEliminada = true
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

    fun onNombreChange(v: String) { nombre = v }
    fun onTelefonoChange(v: String) { telefono = v }
    fun onRegionChange(v: String) { region = v }
    fun onPasswordChange(v: String) { password = v }
}