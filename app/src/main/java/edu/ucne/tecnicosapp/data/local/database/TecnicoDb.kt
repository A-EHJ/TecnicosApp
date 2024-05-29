package edu.ucne.tecnicosapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.tecnicosapp.data.local.dao.ServicioDao
import edu.ucne.tecnicosapp.data.local.dao.TecnicoDao
import edu.ucne.tecnicosapp.data.local.dao.TipoTecnicoDao
import edu.ucne.tecnicosapp.data.local.entities.DateConverter
import edu.ucne.tecnicosapp.data.local.entities.ServicioEntity
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TipoTecnicoEntity::class,
        ServicioEntity::class
    ],
    version = 9,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun tipoTecnicoDao(): TipoTecnicoDao
    abstract fun servicioDao(): ServicioDao
}
