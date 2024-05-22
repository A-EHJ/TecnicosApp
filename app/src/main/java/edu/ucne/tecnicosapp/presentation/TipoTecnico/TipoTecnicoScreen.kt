package edu.ucne.tecnicosapp.presentation.TipoTecnico

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.tecnicosapp.presentation.Tecnico.TopAppBarVolver
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme


@Composable
fun TipoTecnicoScreen(
    viewModel: TipoTecnicoViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TipoTecnicoBody(
        uiState = uiState,
        onDescripcionChanged = viewModel::onDescripcionChanged,
        limpiarTipoTecnico = viewModel::limpiarTipoTecnico,
        onVolver = {
            navController.popBackStack()
        },
        onSaveTipoTecnico = {
            viewModel.saveTipoTecnico()
        },
        onDeleteTipoTecnico = viewModel::deleteTipoTecnico
    )
}

@Composable
fun TipoTecnicoBody(
    uiState: TipoTecnicoUIState,
    onDescripcionChanged: (String) -> Unit,
    limpiarTipoTecnico: () -> Unit,
    onVolver: () -> Unit,
    onSaveTipoTecnico: () -> Unit,
    onDeleteTipoTecnico: () -> Unit
) {
    val context = LocalContext.current
    var guardarVarios by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarVolver(title = "Registro de Tipo Tecnicos", onVolver) }) { innerPadding ->

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
                    label = { Text(text = "descripcion") },
                    value = uiState.descripcion,
                    onValueChange = onDescripcionChanged,
                    isError = !uiState.descripcionError.isNullOrEmpty(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                if (!uiState.descripcionError.isNullOrEmpty()) {
                    Text(text = uiState.descripcionError ?: "", color = Color.Red)
                }


                Spacer(modifier = Modifier.padding(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (uiState.tipoTecnicoId != 0 && uiState.tipoTecnicoId != null ) {
                        OutlinedButton(
                            onClick = {
                                onDeleteTipoTecnico()
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
                            limpiarTipoTecnico()
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
                            onSaveTipoTecnico()
                            if (uiState.descripcionError.isNullOrEmpty()){
                                if (uiState.tipoTecnicoId == 0 || uiState.tipoTecnicoId == null) {
                                    Toast.makeText(context, "Tecnico agregado", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(context, "Tecnico actualizado", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                onVolver()
                            }
                        }
                    ) {
                        if (uiState.tipoTecnicoId == 0 || uiState.tipoTecnicoId == null) {
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
        TipoTecnicoBody(
            uiState = TipoTecnicoUIState(),
            onDescripcionChanged = {},
            limpiarTipoTecnico = {},
            onVolver = {},
            onSaveTipoTecnico = {},
            onDeleteTipoTecnico = {}
        )
    }
}
