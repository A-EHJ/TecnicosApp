package edu.ucne.tecnicosapp.presentation.Tecnico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.myapplication.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel,
    onVerTecnico: (TecnicoEntity) -> Unit
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    TecnicoListBody(
        tecnicos = tecnicos,
        onVerTecnico = onVerTecnico
    )
}
@Composable
fun TecnicoListBody(
    tecnicos: List<TecnicoEntity>,
    onVerTecnico: (TecnicoEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(tecnicos) { tecnico ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onVerTecnico(tecnico) }
                        .padding(16.dp)
                ) {
                    Text(text = tecnico.tecnicoId.toString(), modifier = Modifier.weight(0.10f))
                    Text(text = tecnico.nombres, modifier = Modifier.weight(0.400f))
                    Text(text = tecnico.sueldoHora.toString(), modifier = Modifier.weight(0.40f))
                }
            }
        }
    }
}

@Preview
@Composable
private fun TecnicoListPreview() {
    val tecnicos = listOf(
        TecnicoEntity(
            nombres = "Enel Almonte",
            sueldoHora = 9999.99
        )
    )
    TecnicosAppTheme {
        TecnicoListBody(tecnicos = tecnicos) {
        }
    }
}

