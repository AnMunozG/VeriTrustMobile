package com.example.veritrustmobile.repository

import android.content.Context
import com.example.veritrustmobile.model.Servicio

/**
 * Repository para gestionar el acceso a los datos de los servicios.
 *
 * @param context El contexto de la aplicación, necesario para inicializar la base de datos.
 */
class ServicesRepository(context: Context) {

    private val dbHelper = Database(context)

    /**
     * Obtiene la lista completa de servicios desde la base de datos SQLite.
     *
     * @return Una lista de objetos [Servicio].
     */
    fun getAllServices(): List<Servicio> {
        val db = dbHelper.readableDatabase
        val servicesList = mutableListOf<Servicio>()

        // Consulta a la tabla de servicios para obtener todos los registros.
        val cursor = db.query(
            Database.ServicesTable.TABLE_NAME,
            null, // null devuelve todas las columnas.
            null, null, null, null,
            null  // Sin orden específico.
        )

        // Itera sobre el cursor para construir la lista de objetos Servicio.
        with(cursor) {
            while (moveToNext()) {
                val nombre = getString(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_NOMBRE))
                val descripcion = getString(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_DESCRIPCION))
                val precio = getInt(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_PRECIO))
                val detallesString = getString(getColumnIndexOrThrow(Database.ServicesTable.COLUMN_NAME_DETALLES))

                // Reconvierte el String de detalles a una lista, usando el separador '|'.
                val detallesList = detallesString.split("|")

                servicesList.add(Servicio(nombre, descripcion, precio, detallesList))
            }
        }
        // Es crucial cerrar el cursor para liberar recursos.
        cursor.close()
        return servicesList
    }
}
