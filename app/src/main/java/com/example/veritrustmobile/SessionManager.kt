package com.example.veritrustmobile

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "veritrust_session"
    private const val TOKEN_KEY = "auth_token"
    private lateinit var preferences: SharedPreferences

    // Se inicia una sola vez en el MainActivity
    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Guardar sesión (token o email)
    fun saveToken(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    // Obtener token (si devuelve null, es que no hay sesión)
    fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    // Borrar sesión (Logout)
    fun clearSession() {
        preferences.edit().clear().apply()
    }
}