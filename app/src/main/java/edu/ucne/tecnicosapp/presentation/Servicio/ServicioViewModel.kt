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

    fun deleteServicio(id: Int) {
        viewModelScope.launch {
            servicioRepository.deleteServicioById(id)
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
                        Descripcion = Servicio.Descripcion ?: "",
                        Total = Servicio.Total
                    )
                }
            }
        }
    }

    fun saveServicio() {
        var valido = true

        if (uiState.value.Cliente.isNullOrBlank()) {
            uiState.update {
                it.copy(ClienteError = "El cliente no puede estar vacío")
            }
            valido = false
        }

        if (uiState.value.Descripcion.isNullOrBlank()) {
            uiState.update {
                it.copy(DescripcionError = "La descripción no puede estar vacía")
            }
            valido = false
        }

        if (uiState.value.Total == 0.0) {
            uiState.update {
                it.copy(TotalError = "El total no puede ser 0")
            }
            valido = false
        }

        if (uiState.value.TecnicoId == 0) {
            uiState.update {
                it.copy(TecnicoError = "El técnico no puede estar vacío")
            }
            valido = false
        }

        if (valido) {
            uiState.update {
                it.copy(
                    ClienteError = null,
                    DescripcionError = null,
                    TotalError = null,
                    TecnicoError = null
                )
            }
        } else {
            return
        }


        viewModelScope.launch {

            if (!valido) {
                return@launch
            }

            // Update guardo
            uiState.update {
                it.copy(guardo = true)
            }

            servicioRepository.saveServicio(uiState.value.toEntity())
        }
    }


    fun limpiarServicio() {
        viewModelScope.launch {
            uiState.value = ServicioUIState()
        }
    }


    val regexTotal = Regex("^[0-9]{0,7}(\\.[0-9]{0,2})?$")
    val regexCliente: Regex = Regex("^[a-zA-Z]+(?: [a-zA-Z]+)*$")

    /*los onchanged*/
    fun onTecnicoChanged(TecnicoId: Int) {
        uiState.update {
            it.copy(TecnicoId = TecnicoId)
        }
    }

    fun onClienteChanged(nombres: String) {
        val cleanedCliente = nombres.trim().replace(Regex("\\s+"), " ")
        uiState.update {
            if (cleanedCliente.isEmpty() || regexCliente.matches(cleanedCliente)) {
                it.copy(Cliente = cleanedCliente)
            } else {
                it
            }
        }
    }

    fun onTotalChanged(total: String) {
        if (total.matches(regexTotal)) {
            var Total = total.toDoubleOrNull() ?: 0.0
            if (Total == null) {
                Total = 0.0
            }
            uiState.update {
                it.copy(
                    Total = Total,
                )
            }
        }
    }

    fun onDescripcionChanged(descripcion: String) {
        val cleanedDescripcion = descripcion.trim().replace(Regex("\\s+"), " ")
        uiState.update {
            if (cleanedDescripcion.isEmpty() || regexCliente.matches(cleanedDescripcion)) {
                it.copy(Descripcion = cleanedDescripcion)
            } else {
                it
            }
        }
    }


}


data class ServicioUIState(
    var ServicioId: Int? = null,
    var Fecha: String? = null,
    var TecnicoId: Int? = 0,
    var TecnicoError: String? = "",
    var Cliente: String? = null,
    var ClienteError: String? = null,
    var Descripcion: String = "",
    var DescripcionError: String? = null,
    var Total: Double = 0.0,
    var TotalError: String? = null,
    var guardo: Boolean = false
)

fun ServicioUIState.toEntity(): ServicioEntity {
    return ServicioEntity(
        ServicioId = ServicioId,
        Fecha = Fecha.toString(),
        TecnicoId = TecnicoId,
        Cliente = Cliente,
        Descripcion = Descripcion,
        Total = Total
    )
}