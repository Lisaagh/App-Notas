package com.example.appnotas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val Titulo: String,
    val Contenido: String,
    val Fecha: String,
    val ColorFondo: Long,
    val FondoImagenUri: String? = null
)
