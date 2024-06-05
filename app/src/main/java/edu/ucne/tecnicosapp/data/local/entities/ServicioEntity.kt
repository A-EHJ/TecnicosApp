package edu.ucne.tecnicosapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Servicio")
data class ServicioEntity(
    @PrimaryKey
    val ServicioId: Int? = null,
    val Fecha: String? = null,
    val TecnicoId: Int? = null,
    val Cliente: String? = null,
    val Descripcion: String? = null,
    val Total: Double = 0.0
)
