package edu.ucne.tecnicosapp.data.local.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "TipoTecnico")
data class TipoTecnicoEntity(
    @PrimaryKey
    val tipoTecnicoId: Int? = null,
    var descripcion: String = "",
)