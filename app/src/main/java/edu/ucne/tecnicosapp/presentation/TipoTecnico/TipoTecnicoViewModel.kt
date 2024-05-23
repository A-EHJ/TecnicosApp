package edu.ucne.tecnicosapp.presentation.TipoTecnico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity
import edu.ucne.tecnicosapp.data.repository.TipoTecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

class TipoTecnicoViewModel(
    private val repository: TipoTecnicoRepository,
    private val TipoTecnicoId: Int
) :
    ViewModel() {

    val regexNombre: Regex = Regex("^[a-zA-Z]+(?: [a-zA-Z]+)*$")
    var uiState = MutableStateFlow(TipoTecnicoUIState())
        private set

    val TipoTecnicos = repository.getTipoTecnicos()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )


    fun deleteTipoTecnico() {
        viewModelScope.launch {
            repository.deleteTipoTecnico(uiState.value.toEntity())
        }
    }

    fun deleteTipoTecnico(id: Int) {
        viewModelScope.launch {
            repository.deleteTipoTecnico(id)
        }
    }

    fun onDescripcionChanged(descripcion: String) {
        val cleanedDescripcion = descripcion.trim().replace(Regex("\\s+"), " ")
        uiState.update {
            if (cleanedDescripcion.isEmpty() || regexNombre.matches(cleanedDescripcion)) {
                it.copy(descripcion = cleanedDescripcion.lowercase(Locale.getDefault()))
            } else {
                it
            }
        }
    }


    init {
        viewModelScope.launch {
            val TipoTecnico = repository.getTipoTecnico(TipoTecnicoId)

            TipoTecnico?.let {
                uiState.update {
                    it.copy(
                        tipoTecnicoId = TipoTecnico.tipoTecnicoId ?: 0,
                        descripcion = TipoTecnico.descripcion
                    )
                }
            }
        }
    }

    fun saveTipoTecnico() {
        // Reset errors
        uiState.update {
            it.copy( descripcionError = null)
        }

        // Update tecnicoId if it's 0
        if (uiState.value.tipoTecnicoId == 0) {
            uiState.update {
                it.copy(tipoTecnicoId = null)
            }
        }



        var valido = true

        // Validate nombres
        if (uiState.value.descripcion.isEmpty()) {
            uiState.update {
                it.copy(descripcionError = "El nombre no puede estar vacío")
            }
            valido = false
        }


        viewModelScope.launch {
            if (!descripcionTipoTecnicoExiste()) {
                uiState.update {
                    it.copy(descripcionError = "La descripción ya existe")
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
            if (uiState.value.descripcionError == null) {

                repository.saveTipoTecnico(uiState.value.toEntity())
            }
        }
    }


    suspend fun descripcionTipoTecnicoExiste(): Boolean {
        val tecnico = repository.getTipoTecnico(uiState.value.descripcion, uiState.value.tipoTecnicoId ?: 0)
        return tecnico == null
    }


    fun limpiarTipoTecnico() {
        viewModelScope.launch {
            uiState.update {
                it.copy(
                    tipoTecnicoId = null,
                    descripcion = "",
                    descripcionError = null
                )
            }
        }
    }
}


data class TipoTecnicoUIState(
    val tipoTecnicoId: Int? = null,
    var descripcion: String = "",
    var descripcionError: String? = null,
    var guardo: Boolean = false
)

fun TipoTecnicoUIState.toEntity(): TipoTecnicoEntity {
    return TipoTecnicoEntity(
        tipoTecnicoId = tipoTecnicoId,
        descripcion = descripcion
    )
}