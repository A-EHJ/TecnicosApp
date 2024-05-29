package edu.ucne.tecnicosapp.data.repository

import edu.ucne.tecnicosapp.data.local.dao.ServicioDao
import edu.ucne.tecnicosapp.data.local.entities.ServicioEntity

class ServicioRepository(private val servicioDao: ServicioDao) {
    suspend fun saveServicio(servicio: ServicioEntity) = servicioDao.save(servicio)
    fun getServicios() = servicioDao.getAll()
    suspend fun deleteServicio(servicio: ServicioEntity) = servicioDao.delete(servicio)

    suspend fun deleteServicioById(id: Int) {
        servicioDao.find(id)?.let { servicioDao.delete(it) }
    }

    suspend fun deleteServiciosByIds(ids: List<Int>) {
        ids.forEach { id ->
            servicioDao.find(id)?.let { servicioDao.delete(it) }
        }
    }

    suspend fun getServicio(servicioId: Int) = servicioDao.find(servicioId)
}
