package edu.ucne.tecnicosapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnico")
data class TecnicoEntity(
    @PrimaryKey
    val tecnicoId: Int? = null,
    val nombres: String = "",
    val sueldoHora: Double = 0.0,
    val tipoTecnicoId: Int? = null
)

