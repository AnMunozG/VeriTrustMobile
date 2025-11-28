package com.example.veritrustmobile

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREFS_NAME = "veritrust_session"
    private const val TOKEN_KEY = "auth_token"
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

    fun clearSession() {
        preferences.edit().clear().apply()
    }
}