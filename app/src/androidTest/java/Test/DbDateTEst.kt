package Test

import android.content.Context
import androidx.room.Room
import edu.ucne.tecnicosapp.data.local.database.TecnicoDb
import edu.ucne.tecnicosapp.data.local.entities.ServicioEntity
import kotlinx.coroutines.runBlocking
import java.util.Date

fun testDatabase(context: Context) {
    val db = Room.databaseBuilder(
        context,
        TecnicoDb::class.java, "tecnico-db"
    ).build()

    val servicioDao = db.servicioDao()

    runBlocking {
        val newServicio = ServicioEntity(
            ServicioId = 1,
            Fecha = Date(), // Usar la fecha actual
            TecnicoId = 123.0,
            Cliente = "Cliente Prueba",
            Descripcion = "Descripción de Prueba",
            Total = 100.0
        )

        // Guardar el servicio en la base de datos
        servicioDao.save(newServicio)

        // Recuperar el servicio
        val retrievedServicio = servicioDao.find(1)
        println("Fecha del servicio recuperado: ${retrievedServicio?.Fecha}")
    }
}