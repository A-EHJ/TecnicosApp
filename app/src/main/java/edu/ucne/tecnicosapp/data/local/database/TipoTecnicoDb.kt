package edu.ucne.tecnicosapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.tecnicosapp.data.local.dao.TipoTecnicoDao
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity

//data/local/database
@Database(
    entities = [
        TipoTecnicoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class TipoTecnicoDb : RoomDatabase() {
    abstract fun TipoTecnicoDao(): TipoTecnicoDao
}