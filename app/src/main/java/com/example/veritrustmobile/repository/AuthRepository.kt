package com.example.veritrustmobile.repository

import android.content.ContentValues
import android.content.Context
import com.example.veritrustmobile.model.User
import com.example.veritrustmobile.ui.viewmodel.RegistroViewModel // Importa el ViewModel para acceder a los datos de registro

/**
 * Repository para gestionar las operaciones de autenticación y usuarios.
 * Abstrae el acceso a la base de datos para la lógica de negocio.
 *
 * @param context El contexto de la aplicación, necesario para inicializar la base de datos.
 */
class AuthRepository(context: Context) {

    private val dbHelper = Database(context)

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param viewModel El ViewModel de registro que contiene todos los datos del usuario.
     * @return `true` si la inserción fue exitosa, `false` en caso contrario.
     */
    fun registrarUsuario(viewModel: RegistroViewModel): Boolean {
        val db = dbHelper.writableDatabase

        // Se crea un objeto ContentValues para mapear los datos a las columnas de la tabla.
        val values = ContentValues().apply {
            put(Database.UsersTable.COLUMN_NAME_RUT, viewModel.rut)
            put(Database.UsersTable.COLUMN_NAME_NOMBRE_COMPLETO, viewModel.nombre)
            put(Database.UsersTable.COLUMN_NAME_FECHA_NACIMIENTO, viewModel.fechaNacimiento)
            put(Database.UsersTable.COLUMN_NAME_TELEFONO, viewModel.telefono)
            put(Database.UsersTable.COLUMN_NAME_EMAIL, viewModel.email)
            // IMPORTANTE: En una app real, aquí deberías "hashear" la contraseña.
            put(Database.UsersTable.COLUMN_NAME_PASSWORD_HASH, viewModel.contrasena)
        }
        val newRowId = db.insert(Database.UsersTable.TABLE_NAME, null, values)

        // Se retorna `true` si el ID de la nueva fila es diferente de -1.
        return newRowId != -1L
    }

    /**
     * Busca un usuario por su correo y contraseña. Útil para la pantalla de Login.
     *
     * @param email El correo del usuario.
     * @param password La contraseña en texto plano (en una app real, sería un hash).
     * @return Un objeto [User] si se encuentra, o `null` si no existe o la contraseña no coincide.
     */
    fun findUserByCredentials(email: String, password: String): User? {
        val db = dbHelper.readableDatabase

        val selection = "${Database.UsersTable.COLUMN_NAME_EMAIL} = ? AND ${Database.UsersTable.COLUMN_NAME_PASSWORD_HASH} = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            Database.UsersTable.TABLE_NAME,
            null, // Todas las columnas
            selection,
            selectionArgs,
            null, null, null
        )

        var user: User? = null
        with(cursor) {
            if (moveToFirst()) {
                val userEmail = getString(getColumnIndexOrThrow(Database.UsersTable.COLUMN_NAME_EMAIL))
                val userPassword = getString(getColumnIndexOrThrow(Database.UsersTable.COLUMN_NAME_PASSWORD_HASH))
                user = User(userEmail, userPassword) // Simplificado, podrías construir un objeto User más completo
            }
        }
        cursor.close()
        return user
    }
}
