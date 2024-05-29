package edu.ucne.tecnicosapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "Servicio")
data class ServicioEntity(
    @PrimaryKey
    val ServicioId: Int? = null,
    val Fecha: Date = Date(),
    val TecnicoId: Double = 0.0,
    val Cliente: String? = null,
    val Descripcion: String? = null,
    val Total: Double? = null
)

class DateConverter {
    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(date: Long): Date {
        return Date(date)
    }
}
