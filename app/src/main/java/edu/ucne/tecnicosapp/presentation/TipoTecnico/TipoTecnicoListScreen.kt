package edu.ucne.tecnicosapp.presentation.TipoTecnico


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.tecnicosapp.data.local.entities.TipoTecnicoEntity
import edu.ucne.tecnicosapp.presentation.Component.FloatingActionButtonSimple
import edu.ucne.tecnicosapp.presentation.Tecnico.TopAppBar
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme
import kotlinx.coroutines.launch


@Composable
fun TipoTecnicoListScreen(
    viewModel: TipoTecnicoViewModel,
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    drawerState: DrawerState
) {
    val tipoTecnicos by viewModel.TipoTecnicos.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = "Tipo de Tecnicos",
                onMenuClick = { scope.launch { drawerState.open() } }
            )
        },
        floatingActionButton = {
            FloatingActionButtonSimple(
                onClick = {
                    onVerTipoTecnico(
                        TipoTecnicoEntity(
                            tipoTecnicoId = 0,
                            descripcion = ""
                        )
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
                .padding(it)
        ) {
            TipoTecnicoListBody(
                tipoTecnicos = tipoTecnicos,
                onVerTipoTecnico = onVerTipoTecnico,
                eliminarTipoTecnico = {
                    viewModel.deleteTipoTecnico(it.tipoTecnicoId ?: 0)
                }
            )
        }
    }
}

@Composable
fun TipoTecnicoListBody(
    tipoTecnicos: List<TipoTecnicoEntity>,
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    eliminarTipoTecnico: (TipoTecnicoEntity) -> Unit
) {
    var showDeleteModeDialog by remember { mutableStateOf(false) }
    var modoEliminarOn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.Right


            ) {
                Text(
                    text = "ID",
                    modifier = Modifier
                        .weight(0.10f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center

                )

                Text(
                    text = "Descripción",
                    modifier = Modifier
                        .weight(0.40f)
                )


                IconButton(onClick = {
                    if (!modoEliminarOn) {
                        showDeleteModeDialog = true
                    } else {
                        modoEliminarOn = false
                        Toast.makeText(context, "Modo eliminar desactivado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }) {
                    Icon(
                        if (modoEliminarOn) Icons.TwoTone.Close else Icons.TwoTone.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFC95050)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(tipoTecnicos) { tecnico ->
                TecnicoRow(
                    onVerTipoTecnico,
                    tecnico,
                    modoEliminarOn,
                    eliminarTipoTecnico,
                    context
                )
            }
        }
    }
    if (showDeleteModeDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteModeDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFDAA504)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        "Modo eliminar",
                        style = typography.titleMedium
                    )
                }
            },
            text = {
                Text(
                    "¿Esta seguro de activar el modo eliminar ?",
                    style = typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        modoEliminarOn = true
                        showDeleteModeDialog = false
                        Toast.makeText(context, "Modo eliminar activado", Toast.LENGTH_SHORT)
                            .show()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteModeDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
private fun TecnicoRow(
    onVerTipoTecnico: (TipoTecnicoEntity) -> Unit,
    tipoTecnico: TipoTecnicoEntity,
    modoEliminarOn: Boolean,
    eliminarTipoTecnico: (TipoTecnicoEntity) -> Unit,
    context: Context
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable { onVerTipoTecnico(tipoTecnico) }
        .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            Text(text = tipoTecnico.tipoTecnicoId.toString(), modifier = Modifier.weight(0.10f))
            Text(text = tipoTecnico.descripcion, modifier = Modifier.weight(0.400f))
            if (modoEliminarOn)

                IconButton(
                    onClick = {
                        eliminarTipoTecnico(tipoTecnico)
                        Toast.makeText(context, "Tipo de Tecnico eliminado", Toast.LENGTH_SHORT)
                            .show()
                    },
                    modifier = Modifier.height(23.dp),
                ) {
                    Icon(
                        Icons.TwoTone.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFC95050)
                    )
                }
        }
    }
}


@Preview
@Composable
private fun PreviewTecnicoListBody() {
    val lista = listOf(
        TipoTecnicoEntity(
            tipoTecnicoId = 1,
            descripcion = "Tecnico en sistemas"
        ),
        TipoTecnicoEntity(
            tipoTecnicoId = 2,
            descripcion = "Tecnico en redes"
        ),
    )


    TecnicosAppTheme {

        TipoTecnicoListBody(
            tipoTecnicos = lista,
            onVerTipoTecnico = {},
            eliminarTipoTecnico = {}
        )
    }
}







