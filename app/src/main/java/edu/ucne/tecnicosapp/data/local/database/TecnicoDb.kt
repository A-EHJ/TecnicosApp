package edu.ucne.tecnicosapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ucne.myapplication.data.local.dao.TecnicoDao
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity

//data/local/database
@Database(
    entities = [
        TecnicoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TecnicoDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
}