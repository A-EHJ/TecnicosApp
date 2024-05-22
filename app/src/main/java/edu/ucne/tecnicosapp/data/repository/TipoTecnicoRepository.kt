package edu.ucne.tecnicosapp.data.repository

import edu.ucne.tecnicosapp.data.local.dao.TipoTecnicoDao
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity

class TipoTecnicoRepository(private val tipoTecnicodao: TipoTecnicoDao) {
    suspend fun saveTipoTecnico(tipoTecnico: TipoTecnicoEntity) = tipoTecnicodao.save(tipoTecnico)

    fun getTipoTecnicos() = tipoTecnicodao.getAll()

    suspend fun deleteTipoTecnico(tipoTecnico: TipoTecnicoEntity) =
        tipoTecnicodao.delete(tipoTecnico)

    suspend fun deleteTipoTecnico(id: Int) = tipoTecnicodao.find(id).let { tecnico ->
        tecnico?.let { tipoTecnicodao.delete(it) }
    }


    suspend fun deleteTipoTecnico(ids: List<Int>) {
        ids.forEach { id ->
            tipoTecnicodao.find(id).let { tecnico ->
                tecnico?.let { tipoTecnicodao.delete(it) }
            }
        }
    }

    suspend fun getTipoTecnico(tipoTecnicoId: Int) = tipoTecnicodao.find(tipoTecnicoId)

    suspend fun getTipoTecnico(descripcion: String, tipoTecnicoId: Int) = tipoTecnicodao.find(descripcion.toLowerCase().replace(" ", ""), tipoTecnicoId)

}