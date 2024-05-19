package com.ucne.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TecnicoDao {
    @Upsert()
    suspend fun save(tecnico: TecnicoEntity)

    @Query(
        """
        SELECT * 
        FROM Tecnico 
        WHERE tecnicoId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): TecnicoEntity?

    @Delete
    suspend fun delete(tecnico: TecnicoEntity)

    @Query("SELECT * FROM Tecnico")
    fun getAll(): Flow<List<TecnicoEntity>>
}
