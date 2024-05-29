package edu.ucne.tecnicosapp.presentation.Servicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.tecnicosapp.data.local.entities.ServicioEntity
import edu.ucne.tecnicosapp.data.repository.ServicioRepository
import edu.ucne.tecnicosapp.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ServicioViewModel(
    private val servicioRepository: ServicioRepository,
    private val ServicioId: Int,
    private val TecnicoRepository: TecnicoRepository
) :
    ViewModel() {

    val Tecnicos = TecnicoRepository.getTecnicos()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    var uiState = MutableStateFlow(ServicioUIState())
        private set

    val servicios = servicioRepository.getServicios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun deleteServicio() {
        viewModelScope.launch {
            servicioRepository.deleteServicio(uiState.value.toEntity())
        }
    }

    init {
        viewModelScope.launch {
            val Servicio = servicioRepository.getServicio(ServicioId)

            Servicio?.let {
                uiState.update {
                    it.copy(
                        ServicioId = Servicio.ServicioId,
                        Fecha = Servicio.Fecha,
                        TecnicoId = Servicio.TecnicoId,
                        Cliente = Servicio.Cliente,
                        Descripcion = Servicio.Descripcion,
                        Total = Servicio.Total
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        viewModelScope.launch {
            servicioRepository.saveServicio(uiState.value.toEntity())
        }
    }


    fun limpiarTecnico() {
        viewModelScope.launch {
            uiState.value = ServicioUIState()
        }
    }
}


data class ServicioUIState(
    var ServicioId: Int? = null,
    var Fecha: Date = Date(),
    var TecnicoId: Double = 0.0,
    var TecnicoError: String? = "",
    var Cliente: String? = null,
    var ClienteError: String? = null,
    var Descripcion: String? = null,
    var DescripcionError: String? = null,
    var Total: Double? = null,
    var TotalError: String? = null
)

fun ServicioUIState.toEntity(): ServicioEntity {
    return ServicioEntity(
        ServicioId = ServicioId,
        Fecha = Fecha,
        TecnicoId = TecnicoId,
        Cliente = Cliente,
        Descripcion = Descripcion,
        Total = Total
    )
}