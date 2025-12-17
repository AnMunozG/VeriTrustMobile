package com.example.veritrustmobile

import android.content.Context
import android.content.SharedPreferences

// app/src/main/java/com/example/veritrustmobile/SessionManager.kt

object SessionManager {
    private const val PREFS_NAME = "veritrust_session"
    private const val TOKEN_KEY = "auth_token"
    private const val ROL_KEY = "user_rol" // ⭐ NUEVO
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    // ⭐ NUEVAS FUNCIONES PARA ROL
    fun saveRol(rol: String) {
        preferences.edit().putString(ROL_KEY, rol).apply()
    }

    fun getRol(): String {
        return preferences.getString(ROL_KEY, "invitado") ?: "invitado"
    }

    fun clearSession() {
        preferences.edit().clear().apply()
    }
}