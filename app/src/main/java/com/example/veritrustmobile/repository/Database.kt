package com.example.veritrustmobile.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "veritrust.db"
        private const val DATABASE_VERSION = 1
    }

    object UsersTable : BaseColumns {
        const val TABLE_NAME = "users"
        const val COLUMN_NAME_RUT = "rut"
        const val COLUMN_NAME_NOMBRE_COMPLETO = "nombre_completo"
        const val COLUMN_NAME_FECHA_NACIMIENTO = "fecha_nacimiento"
        const val COLUMN_NAME_TELEFONO = "telefono"
        const val COLUMN_NAME_EMAIL = "email"
        const val COLUMN_NAME_PASSWORD_HASH = "password_hash"

        const val SQL_CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                ${BaseColumns._ID} INTEGER PRIMARY KEY,
                $COLUMN_NAME_RUT TEXT NOT NULL UNIQUE,
                $COLUMN_NAME_NOMBRE_COMPLETO TEXT NOT NULL,
                $COLUMN_NAME_FECHA_NACIMIENTO TEXT NOT NULL,
                $COLUMN_NAME_TELEFONO TEXT,
                $COLUMN_NAME_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_NAME_PASSWORD_HASH TEXT NOT NULL
            )"""
    }

    object ServicesTable : BaseColumns {
        const val TABLE_NAME = "services"
        const val COLUMN_NAME_NOMBRE = "nombre"
        const val COLUMN_NAME_DESCRIPCION = "descripcion"
        const val COLUMN_NAME_PRECIO = "precio"
        const val COLUMN_NAME_DETALLES = "detalles"

        const val SQL_CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                ${BaseColumns._ID} INTEGER PRIMARY KEY,
                $COLUMN_NAME_NOMBRE TEXT NOT NULL,
                $COLUMN_NAME_DESCRIPCION TEXT,
                $COLUMN_NAME_PRECIO INTEGER,
                $COLUMN_NAME_DETALLES TEXT
            )"""
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UsersTable.SQL_CREATE_TABLE)
        db.execSQL(ServicesTable.SQL_CREATE_TABLE)
        addInitialLoginUsers(db)
        addInitialServices(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${UsersTable.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${ServicesTable.TABLE_NAME}")
        onCreate(db)
    }

    private fun addInitialLoginUsers(db: SQLiteDatabase) {
        val initialUsers = listOf(
            mapOf("rut" to "1-9", "nombre" to "Dilan", "fecha" to "01/01/2000", "telefono" to "123", "email" to "dilan@test.com", "password" to "dilan123"),
            mapOf("rut" to "2-7", "nombre" to "Angel", "fecha" to "15/05/1998", "telefono" to "123", "email" to "angel@test.com", "password" to "angel123"),
            mapOf("rut" to "3-5", "nombre" to "Admin", "fecha" to "20/02/1995", "telefono" to "123", "email" to "admin@test.com", "password" to "admin")
        )

        db.beginTransaction()
        try {
            initialUsers.forEach { userData ->
                val values = ContentValues().apply {
                    put(UsersTable.COLUMN_NAME_RUT, userData["rut"])
                    put(UsersTable.COLUMN_NAME_NOMBRE_COMPLETO, userData["nombre"])
                    put(UsersTable.COLUMN_NAME_FECHA_NACIMIENTO, userData["fecha"])
                    put(UsersTable.COLUMN_NAME_TELEFONO, userData["telefono"])
                    put(UsersTable.COLUMN_NAME_EMAIL, userData["email"])
                    put(UsersTable.COLUMN_NAME_PASSWORD_HASH, userData["password"])
                }
                db.insert(UsersTable.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    // La función addInitialServices no cambia.
    private fun addInitialServices(db: SQLiteDatabase) {
        val initialServices = listOf(
            mapOf(
                "nombre" to "Firma Electrónica Simple",
                "descripcion" to "Certificado Digital",
                "precio" to 15390,
                "detalles" to listOf(
                    "Permite realizar Facturación en el SII.",
                    "Centralización Automática del certificado en el sistema del SII.",
                    "Compra en menos de 5 minutos. Renovación Express.",
                    "Firma documentos tributarios electrónicos como facturas y notas de crédito.",
                    "Lleva 1 año gratis en tu certificado digital. 3×2 años."
                )
            ),
            mapOf(
                "nombre" to "Firma Electrónica Avanzada Online",
                "descripcion" to "e-token",
                "precio" to 21990,
                "detalles" to listOf(
                    "Permite firmar documentos ilimitados desde cualquier lugar.",
                    "Compra 100% online con ClaveÚnica y Cédula Vigente.",
                    "Adquisición instantánea e implementación fácil.",
                    "Valida para todo tipo de documentos legales y comerciales."
                )
            ),
            mapOf(
                "nombre" to "Firma Electrónica Avanzada Notario",
                "descripcion" to "Uso profesional",
                "precio" to 42990,
                "detalles" to listOf(
                    "Requiere Certificado de la Corte de Apelaciones.",
                    "Firma portable en dispositivo e-token.",
                    "Alta seguridad y validez legal."
                )
            )
        )

        db.beginTransaction()
        try {
            initialServices.forEach { serviceData ->
                val values = ContentValues().apply {
                    put(ServicesTable.COLUMN_NAME_NOMBRE, serviceData["nombre"] as String)
                    put(ServicesTable.COLUMN_NAME_DESCRIPCION, serviceData["descripcion"] as String)
                    put(ServicesTable.COLUMN_NAME_PRECIO, serviceData["precio"] as Int)
                    @Suppress("UNCHECKED_CAST")
                    val detallesList = serviceData["detalles"] as List<String>
                    put(ServicesTable.COLUMN_NAME_DETALLES, detallesList.joinToString("|"))
                }
                db.insert(ServicesTable.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}
