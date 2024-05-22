package edu.ucne.tecnicosapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.myapplication.data.local.dao.TecnicoDao
import edu.ucne.tecnicosapp.data.local.dao.TipoTecnicoDao
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity

//data/local/database
@Database(
    entities = [
        TecnicoEntity::class,
        TipoTecnicoEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun tipoTecnicoDao(): TipoTecnicoDao
}