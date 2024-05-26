package edu.ucne.tecnicosapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TipoTecnicoDao {
    @Upsert()
    suspend fun save(tipoTecnico: TipoTecnicoEntity)

    @Query(
        """
        SELECT * 
        FROM TipoTecnico 
        WHERE tipoTecnicoId=:id  
        LIMIT 1
        """
    )
    suspend fun find(id: Int): TipoTecnicoEntity?


    @Query(
        """
        SELECT * 
        FROM TipoTecnico 
        WHERE descripcion=:descripcion AND tipoTecnicoId!=:tipoTecnicoId
        LIMIT 1
        """
    )
    suspend fun find(descripcion: String, tipoTecnicoId: Int): TipoTecnicoEntity?

    @Query(
        """
        SELECT TipoTecnicoId 
        FROM TipoTecnico 
        WHERE descripcion=:descripcion
        LIMIT 1
        """
    )
    suspend fun find(descripcion: String): Int?


    @Delete
    suspend fun delete(tipoTecnico: TipoTecnicoEntity)

    @Query("SELECT * FROM TipoTecnico")
    fun getAll(): Flow<List<TipoTecnicoEntity>>
}
