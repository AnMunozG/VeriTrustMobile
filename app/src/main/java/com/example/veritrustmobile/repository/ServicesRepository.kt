package com.example.veritrustmobile.repository

import android.content.Context
import com.example.veritrustmobile.model.Servicio


class ServicesRepository(context: Context) {

    private val dbHelper = Database(context)

    fun getAllServices(): List<Servicio> {
        val db = dbHelper.readableDatabase
        val servicesList = mutableListOf<Servicio>()

        val cursor = db.query(
            Database.ServicesTable.TABLE_NAME,
            null, // null devuelve todas las columnas.
            null, null, null, null,
            null  // Sin orden específico.
        )

        with(cursor) {
            while (moveToNext()) {
                val nombre = getString(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_NOMBRE))
                val descripcion = getString(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_DESCRIPCION))
                val precio = getInt(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_PRECIO))
                val detallesString = getString(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_DETALLES))

                val detallesList = detallesString.split("|")

                servicesList.add(Servicio(nombre, descripcion, precio, detallesList))
            }
        }
        cursor.close()
        return servicesList
    }
}
