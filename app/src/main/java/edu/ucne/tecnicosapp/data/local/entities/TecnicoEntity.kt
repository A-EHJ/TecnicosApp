package com.ucne.myapplication.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tecnico")
data class TecnicoEntity(
    @PrimaryKey
    val tecnicoId: Int? = null,
    var nombres: String = "",
    var sueldoHora: Double = 0.0
)