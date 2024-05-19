package edu.ucne.tecnicosapp.presentation.Tecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TecnicoViewModel(private val repository: TecnicoRepository, private val TecnicoId: Int) :
    ViewModel() {

    var uiState = MutableStateFlow(TecnicoUIState())
        private set

    val tecnicos = repository.getTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val regexNombre: Regex = Regex("^[a-zA-Z]+(?: [a-zA-Z]+)*$")


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
        val regex = Regex("[0-9]*\\.?[0-9]{0,2}")
        if (sueldoHoraStr.matches(regex)) {
            val sueldoHora =
                if (sueldoHoraStr == "") null else sueldoHoraStr.toDoubleOrNull() ?: 0.0
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
        fun saveTecnico() {
            viewModelScope.launch {
                uiState.update {
                    it.copy(nombresError = null, sueldoError = null)
                }.let {
                    var valido = true
                    if (uiState.value.nombres.isEmpty()) {
                        uiState.update {
                            it.copy(nombresError = "El nombre no puede estar vacío")
                        }
                        valido = false
                    }
                    if (uiState.value.sueldoHora == 0.0) {
                        uiState.update {
                            it.copy(sueldoError = "El sueldo no puede ser 0")
                        }
                        valido = false
                    }


                    if (!valido) {
                        return@launch // Correct way to exit the coroutine early
                    }



                    if (uiState.value.sueldoError == null && uiState.value.nombresError == null) {
                        repository.saveTecnico(uiState.value.toEntity())
                    }
                }
            }
        }

    }

    fun limpiarTecnico() {
        uiState.update {
            it.copy(
                tecnicoId = 0,
                nombres = "",
                sueldoHora = 0.0,
                sueldoError = null,
                nombresError = null
            )
        }
    }
}


data class TecnicoUIState(
    val tecnicoId: Int = 0,
    var nombres: String = "",
    var nombresError: String? = null,
    var sueldoHora: Double? = 0.0,
    var sueldoError: String? = null,
)

fun TecnicoUIState.toEntity(): TecnicoEntity {
    return TecnicoEntity(
        tecnicoId = tecnicoId,
        nombres = nombres,
        sueldoHora = sueldoHora ?: 0.0,
    )
}