package edu.ucne.tecnicosapp.data.repository

import edu.ucne.tecnicosapp.data.local.dao.TecnicoDao
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity

class TecnicoRepository(private val tecnicoDao: TecnicoDao) {
    suspend fun saveTecnico(tecnico: TecnicoEntity) = tecnicoDao.save(tecnico)

    fun getTecnicos() = tecnicoDao.getAll()

    suspend fun deleteTecnico(tecnico: TecnicoEntity) = tecnicoDao.delete(tecnico)
    suspend fun deleteTecnico(id: Int) = tecnicoDao.find(id).let { tecnico ->
        tecnico?.let { tecnicoDao.delete(it) }
    }

    suspend fun deleteTecnico(ids: List<Int>) {
        ids.forEach { id ->
            tecnicoDao.find(id).let { tecnico ->
                tecnico?.let { tecnicoDao.delete(it) }
            }
        }
    }

    suspend fun getTecnico(tecnicoId: Int) = tecnicoDao.find(tecnicoId)

    suspend fun getTecnico(nombres: String, tecnicoId: Int) = tecnicoDao.find(nombres, tecnicoId)


}