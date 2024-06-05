package edu.ucne.tecnicosapp.presentation.Servicio

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
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.presentation.Component.DropDownInput2
import edu.ucne.tecnicosapp.presentation.Tecnico.TopAppBarVolver
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme


@Composable
fun ServicioScreen(
    viewModel: ServicioViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tecnicos = viewModel.Tecnicos.collectAsStateWithLifecycle().value
    TecnicoBody(
        uiState = uiState,
        limpiarServicio = viewModel::limpiarServicio,
        onVolver = {
            navController.popBackStack()
        },
        onSaveServicio = {
            viewModel.saveServicio()
        },
        onDeleteServicio = viewModel::deleteServicio,
        Tecnicos = tecnicos,
        onTecnicoChanged = viewModel::onTecnicoChanged,
        onTotalChanged = viewModel::onTotalChanged,
        onClientesChanged = viewModel::onClienteChanged,
        onDescripcionChanged = viewModel::onDescripcionChanged
    )
}

@Composable
fun TecnicoBody(
    uiState: ServicioUIState,
    limpiarServicio: () -> Unit,
    onVolver: () -> Unit,
    onSaveServicio: () -> Unit,
    onDeleteServicio: () -> Unit,
    Tecnicos: List<TecnicoEntity>,
    onTecnicoChanged: (Int) -> Unit,
    onTotalChanged: (String) -> Unit,
    onClientesChanged: (String) -> Unit,
    onDescripcionChanged: (String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBarVolver(title = "Registro de Servicios", onVolver) }) { innerPadding ->

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
                    label = { Text(text = "Cliente") },
                    value = uiState.Cliente ?: "",
                    onValueChange = onClientesChanged,
                    isError = !uiState.ClienteError.isNullOrEmpty(),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )


                if (!uiState.ClienteError.isNullOrEmpty()) {
                    Text(text = uiState.ClienteError ?: "", color = Color.Red)
                }


                DropDownInput2(
                    items = Tecnicos,
                    label = "Técnico",
                    itemToString = { it.nombres },
                    onItemSelected = {
                        onTecnicoChanged(it.tipoTecnicoId ?: 0)
                    },
                    itemToId = { it.tipoTecnicoId },
                    selectedItemId = uiState.TecnicoId,
                    isError = !uiState.TecnicoError.isNullOrEmpty()
                )

                if (!uiState.TecnicoError.isNullOrEmpty()) {
                    Text(text = uiState.TecnicoError ?: "", color = Color.Red)
                }



                OutlinedTextField(
                    label = { Text(text = "Total") },
                    value = uiState.Total.toString(),
                    onValueChange = { onTotalChanged(it) },
                    isError = !uiState.TotalError.isNullOrEmpty(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )


                if (!uiState.TotalError.isNullOrEmpty()) {
                    Text(text = uiState.TotalError ?: "", color = Color.Red)
                }



                OutlinedTextField(
                    label = { Text(text = "Descripción") },
                    value = uiState.Descripcion,
                    onValueChange = { onDescripcionChanged(it) },
                    isError = !uiState.DescripcionError.isNullOrEmpty(),
                    modifier = Modifier
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    )
                )

                if (!uiState.DescripcionError.isNullOrEmpty()) {
                    Text(text = uiState.DescripcionError ?: "", color = Color.Red)
                }


                Spacer(modifier = Modifier.padding(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (uiState.ServicioId != 0 && uiState.ServicioId != null) {
                        OutlinedButton(
                            onClick = {
                                onDeleteServicio()
                                Toast.makeText(context, "Servicio eliminado", Toast.LENGTH_SHORT)
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
                            limpiarServicio()
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
                            onSaveServicio()

                            if (uiState.guardo && uiState.TotalError.isNullOrEmpty() && uiState.ClienteError.isNullOrEmpty() && uiState.TecnicoError.isNullOrEmpty() && uiState.DescripcionError.isNullOrEmpty()) {
                                if (uiState.ServicioId != 0) {

                                    Toast.makeText(
                                        context,
                                        "Servicio actualizado",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    Toast.makeText(context, "Servicio agregado", Toast.LENGTH_SHORT)
                                        .show()
                                }
                                limpiarServicio()
                                onVolver()
                            }
                        }
                    ) {
                        if (uiState.ServicioId == 0 || uiState.ServicioId == null) {
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

    }
}
