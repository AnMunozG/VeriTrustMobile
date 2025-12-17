package com.example.veritrustmobile.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.veritrustmobile.data.local.Converters

@Entity(tableName = "servicios")
@TypeConverters(Converters::class)
data class Servicio(
    @PrimaryKey
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val detalles: List<String>
)