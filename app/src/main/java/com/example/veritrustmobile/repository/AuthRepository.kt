package com.example.veritrustmobile.repository

import android.content.ContentValues
import android.content.Context
import com.example.veritrustmobile.model.User

class AuthRepository(context: Context) {

    private val dbHelper = Database(context)
    fun registrarUsuario(
        rut: String,
        nombre: String,
        fechaNacimiento: String,
        telefono: String,
        email: String,
        contrasena: String
    ): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(Database.UsersTable.COLUMN_NAME_RUT, rut)
            put(Database.UsersTable.COLUMN_NAME_NOMBRE_COMPLETO, nombre)
            put(Database.UsersTable.COLUMN_NAME_FECHA_NACIMIENTO, fechaNacimiento)
            put(Database.UsersTable.COLUMN_NAME_TELEFONO, telefono)
            put(Database.UsersTable.COLUMN_NAME_EMAIL, email)
            put(Database.UsersTable.COLUMN_NAME_PASSWORD_HASH, contrasena)
        }

        val newRowId = db.insert(Database.UsersTable.TABLE_NAME, null, values)
        return newRowId != -1L
    }

    /**
     * Busca un usuario por su correo y contraseña.
     */
    fun findUserByCredentials(email: String, password: String): User? {
        val db = dbHelper.readableDatabase
        val selection = "${Database.UsersTable.COLUMN_NAME_EMAIL} = ? AND ${Database.UsersTable.COLUMN_NAME_PASSWORD_HASH} = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(Database.UsersTable.TABLE_NAME, null, selection, selectionArgs, null, null, null)
        var user: User? = null
        with(cursor) {
            if (moveToFirst()) {
                val userEmail = getString(getColumnIndexOrThrow(Database.UsersTable.COLUMN_NAME_EMAIL))
                user = User(userEmail, "")
            }
        }
        cursor.close()
        return user
    }
}
