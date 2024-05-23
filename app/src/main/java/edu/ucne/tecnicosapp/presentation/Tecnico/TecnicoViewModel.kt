package edu.ucne.tecnicosapp.presentation.Tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.data.repository.TecnicoRepository
import edu.ucne.tecnicosapp.data.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TecnicoViewModel(private val repository: TecnicoRepository, private val TecnicoId: Int, private val tipoRepository: TipoTecnicoRepository) :
    ViewModel() {

    val regexSueldoHora = Regex("^[0-9]{0,7}(\\.[0-9]{0,2})?$")
    val regexNombre: Regex = Regex("^[a-zA-Z]+(?: [a-zA-Z]+)*$")


    val tipos = tipoRepository.getTipoTecnicos()
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    var uiState = MutableStateFlow(TecnicoUIState())
        private set

    val tecnicos = repository.getTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onTipoTecnicoChanged(tipoTecnico: String) {
        uiState.update {
            it.copy(tipoTecnico = tipoTecnico)
        }
    }


    fun deleteTecnico() {
        viewModelScope.launch {
            repository.deleteTecnico(uiState.value.toEntity())
        }
    }

    fun deleteTecnico(id: Int) {
        viewModelScope.launch {
            repository.deleteTecnico(id)
        }
    }

    fun onNombresChanged(nombres: String) {
        val cleanedNombres = nombres.trim().replace(Regex("\\s+"), " ")
        uiState.update {
            if (cleanedNombres.isEmpty() || regexNombre.matches(cleanedNombres)) {
                it.copy(nombres = cleanedNombres)
            } else {
                it
            }
        }
    }

    fun onSueldoHoraChanged(sueldoHoraStr: String) {
        if (sueldoHoraStr.matches(regexSueldoHora)) {
            val sueldoHora = sueldoHoraStr.toDoubleOrNull() ?: 0.0
            uiState.update {
                it.copy(
                    sueldoHora = sueldoHora,
                )
            }
        }
    }


    init {
        viewModelScope.launch {
            val Tecnico = repository.getTecnico(TecnicoId)

            Tecnico?.let {
                uiState.update {
                    it.copy(
                        tecnicoId = Tecnico.tecnicoId ?: 0,
                        nombres = Tecnico.nombres,
                        sueldoHora = Tecnico.sueldoHora
                    )
                }
            }
        }
    }

    fun saveTecnico() {
        // Reset errors
        uiState.update {
            it.copy(nombresError = null, sueldoError = null)
        }

        // Update tecnicoId if it's 0
        if (uiState.value.tecnicoId == 0) {
            uiState.update {
                it.copy(tecnicoId = null)
            }
        }


        var valido = true

        // Validate nombres
        if (uiState.value.nombres.isEmpty()) {
            uiState.update {
                it.copy(nombresError = "El nombre no puede estar vacío")
            }
            valido = false
        }



        // Validate sueldoHora
        if (uiState.value.sueldoHora == 0.0) {
            uiState.update {
                it.copy(sueldoError = "El sueldo no puede ser 0")
            }
            valido = false
        }

        // Validate tipoTecnico
        if (uiState.value.tipoTecnico.isNullOrEmpty()) {
            uiState.update {
                it.copy(tipoTecnicoError = "El tipo no puede estar vacío")
            }
        }




        viewModelScope.launch {

            // Check if nombreTecnicoExiste and update valido accordingly
            if (!nombreTecnicoExiste()) {
                uiState.update {
                    it.copy(nombresError = "El nombre ya existe")
                }
                valido = false
            }

            // Early exit if not valid
            if (!valido) {
                return@launch
            }

            // Update guardo
            uiState.update {
                it.copy(guardo = true)
            }

            // Proceed to save if there are no errors
            if (uiState.value.sueldoError == null || uiState.value.nombresError == null) {
                repository.saveTecnico(uiState.value.toEntity())
            }
        }
    }


    suspend fun nombreTecnicoExiste(): Boolean {
        val tecnico = repository.getTecnico(uiState.value.nombres, uiState.value.tecnicoId ?: 0)
        return tecnico == null
    }


    fun limpiarTecnico() {
        viewModelScope.launch {
            uiState.value = TecnicoUIState()
        }
    }
}


data class TecnicoUIState(
    val tecnicoId: Int? = null,
    var nombres: String = "",
    var nombresError: String? = null,
    var sueldoHora: Double? = 0.0,
    var sueldoError: String? = null,
    var tipoTecnico: String? = "",
    var tipoTecnicoError: String? = "",
    var guardo: Boolean = false
)

fun TecnicoUIState.toEntity(): TecnicoEntity {
    return TecnicoEntity(
        tecnicoId = tecnicoId,
        nombres = nombres,
        sueldoHora = sueldoHora ?: 0.0,
    )
}