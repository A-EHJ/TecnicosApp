package edu.ucne.tecnicosapp.presentation.Tecnico

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity
import edu.ucne.tecnicosapp.presentation.Component.DropDownInput2
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme


@Composable
fun TecnicoScreen(
    tecnicoViewModel: TecnicoViewModel,

    navController: NavController
) {
    val uiState by tecnicoViewModel.uiState.collectAsStateWithLifecycle()
    val tipos = tecnicoViewModel.tiposTecnicos.collectAsStateWithLifecycle().value
    TecnicoBody(
        uiState = uiState,
        onsueldoHoraChanged = tecnicoViewModel::onSueldoHoraChanged,
        onNombresChanged = tecnicoViewModel::onNombresChanged,
        limpiarTecnico = tecnicoViewModel::limpiarTecnico,
        onVolver = {
            navController.popBackStack()
        },
        onSaveTecnico = {
            tecnicoViewModel.saveTecnico()
        },
        onDeleteTecnico = tecnicoViewModel::deleteTecnico,
        tipoTecnico = tipos,
           onTipoTecnicoChanged = tecnicoViewModel::onTipoTecnicoChanged


    )
}

@Composable
fun TecnicoBody(
    uiState: TecnicoUIState,
    onsueldoHoraChanged: (String) -> Unit,
    onNombresChanged: (String) -> Unit,
    limpiarTecnico: () -> Unit,
    onVolver: () -> Unit,
    onSaveTecnico: () -> Unit,
    onDeleteTecnico: () -> Unit,
    tipoTecnico: List<TipoTecnicoEntity>,
    onTipoTecnicoChanged: (String)-> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarVolver(title = "Registro de Tecnicos", onVolver) }) { innerPadding ->

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                OutlinedTextField(
                    label = { Text(text = "Nombres") },
                    value = uiState.nombres,
                    onValueChange = onNombresChanged,
                    isError = !uiState.nombresError.isNullOrEmpty(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )


                if (!uiState.nombresError.isNullOrEmpty()) {
                    Text(text = uiState.nombresError ?: "", color = Color.Red)
                }

                OutlinedTextField(
                    label = { Text(text = "Sueldo por hora") },
                    value = uiState.sueldoHora.toString(),
                    onValueChange = { onsueldoHoraChanged(it) },
                    isError = !uiState.sueldoError.isNullOrEmpty(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                )


                if (!uiState.sueldoError.isNullOrEmpty()) {
                    Text(text = uiState.sueldoError ?: "", color = Color.Red)
                }


                DropDownInput2(
                    items = tipoTecnico,
                    label = "Tipos Técnico",
                    itemToString = { it.descripcion},
                    onItemSelected = {
                        onTipoTecnicoChanged(it.descripcion)
                    },
                    itemToId = { it.tipoTecnicoId },
                    selectedItemId = uiState.tipoTecnicoId,
                    isError = !uiState.tipoTecnicoError.isNullOrEmpty()
                )
                if (!uiState.tipoTecnicoError.isNullOrEmpty()) {
                    Text(text = uiState.tipoTecnicoError ?: "", color = Color.Red)
                }


                Spacer(modifier = Modifier.padding(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (uiState.tecnicoId != 0 && uiState.tecnicoId != null) {
                        OutlinedButton(
                            onClick = {
                                onDeleteTecnico()
                                Toast.makeText(context, "Tecnico eliminado", Toast.LENGTH_SHORT)
                                    .show()
                                onVolver()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "delete button"
                            )
                            Text(text = "Eliminar")
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            limpiarTecnico()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "new button"
                        )
                        Text(text = "Nuevo")
                    }
                    OutlinedButton(
                        onClick = {
                            onSaveTecnico()
                            if (uiState.guardo || (!uiState.nombresError.isNullOrEmpty() || !uiState.sueldoError.isNullOrEmpty())) {
                                if (uiState.tecnicoId != 0) {

                                    Toast.makeText(
                                        context,
                                        "Tecnico actualizado",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    Toast.makeText(context, "Tecnico agregado", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                onVolver()
                                limpiarTecnico()
                            }
                        }
                    ) {
                        if (uiState.tecnicoId == 0 || uiState.tecnicoId == null) {
                            Text(text = "Guardar")
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "save button"
                            )
                        } else {
                            Text(text = "Actualizar")
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "save button"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTecnicoBody() {
    TecnicosAppTheme {
        TecnicoBody(
            uiState = TecnicoUIState(),
            onsueldoHoraChanged = {},
            onNombresChanged = {},
            limpiarTecnico = {},
            onVolver = {},
            onSaveTecnico = {},
            onDeleteTecnico = {},
            tipoTecnico = listOf(),
            onTipoTecnicoChanged = {}
        )
    }
}
