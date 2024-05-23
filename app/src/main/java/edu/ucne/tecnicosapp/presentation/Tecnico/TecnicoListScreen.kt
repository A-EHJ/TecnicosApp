package edu.ucne.tecnicosapp.presentation.Tecnico


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
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
import androidx.navigation.NavHostController
import edu.ucne.tecnicosapp.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.presentation.Component.FloatingActionButtonSimple
import edu.ucne.tecnicosapp.presentation.Component.NavigationDrawer
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme
import kotlinx.coroutines.launch


@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit,
    navController: NavHostController
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    NavigationDrawer(navController = navController, drawerState = drawerState) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = "Técnicos",
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            floatingActionButton = {
                FloatingActionButtonSimple(
                    onClick = {
                        onVerTecnico(
                            TecnicoEntity(
                                tecnicoId = null,
                                nombres = "",
                                sueldoHora = 0.0
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
                TecnicoListBody(
                    tecnicos = tecnicos,
                    onVerTecnico = onVerTecnico,
                    eliminarTecnico = { tecnico ->
                        viewModel.deleteTecnico(tecnico.tecnicoId ?: 0)
                    }
                )
            }
        }
    }
}

        @Composable
        fun TecnicoListBody(
            tecnicos: List<TecnicoEntity>,
            onVerTecnico: (TecnicoEntity) -> Unit,
            eliminarTecnico: (TecnicoEntity) -> Unit
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
                        .background(Color(0xFFE0E0E0))
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
                            textAlign = androidx.compose.ui.text.style.TextAlign.Right
                        )
                        Text(
                            text = "Nombres",
                            modifier = Modifier
                                .weight(0.40f)
                                .padding(start = 10.dp)

                        )
                        Text(
                            text = "Sueldo",
                            modifier = Modifier
                                .weight(0.40f)
                                .weight(0.40f)
                                .padding(start = 10.dp)
                        )

                        Text(
                            text = "Tipo",
                            modifier = Modifier
                                .weight(0.30f)
                        )

                        IconButton(onClick = {
                            if (!modoEliminarOn) {
                                showDeleteModeDialog = true
                            } else {
                                modoEliminarOn = false
                                Toast.makeText(
                                    context,
                                    "Modo eliminar desactivado",
                                    Toast.LENGTH_SHORT
                                )
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
                    items(tecnicos) { tecnico ->
                        TecnicoRow(onVerTecnico, tecnico, modoEliminarOn, eliminarTecnico, context)
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
                                Toast.makeText(
                                    context,
                                    "Modo eliminar activado",
                                    Toast.LENGTH_SHORT
                                )
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
            onVerTecnico: (TecnicoEntity) -> Unit,
            tecnico: TecnicoEntity,
            modoEliminarOn: Boolean,
            eliminarTecnico: (TecnicoEntity) -> Unit,
            context: Context
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clickable { onVerTecnico(tecnico) }
                .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)

                ) {
                    Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = tecnico.nombres, modifier = Modifier.weight(0.400f))
                    Text(
                        text = tecnico.sueldoHora.toString(),
                        modifier = Modifier.weight(0.40f)
                    )
                    Text(text = tecnico.tipoTecnico ?: "No Especificado", modifier = Modifier.weight(0.30f))
                    if (modoEliminarOn)

                        IconButton(
                            onClick = {
                                eliminarTecnico(tecnico)
                                Toast.makeText(context, "Tecnico eliminado", Toast.LENGTH_SHORT)
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
                TecnicoEntity(
                    tecnicoId = 1,
                    nombres = "Juan Perez",
                    sueldoHora = 100.0
                ),
                TecnicoEntity(
                    tecnicoId = 2,
                    nombres = "Pedro Perez",
                    sueldoHora = 200.0
                )
            )
            TecnicosAppTheme {
                TecnicoListBody(
                    tecnicos = lista,
                    onVerTecnico = {},
                    eliminarTecnico = {},
                )
            }
        }







