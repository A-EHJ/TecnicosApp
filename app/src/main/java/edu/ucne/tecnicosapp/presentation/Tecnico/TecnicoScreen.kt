package edu.ucne.tecnicosapp.presentation.Tecnico

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.myapplication.data.local.entities.TecnicoEntity
import edu.ucne.tecnicosapp.MainActivity
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme


@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    TecnicoBody(
        onSaveTecnico = { tecnico ->
            viewModel.saveTecnico(tecnico)
        }
    )
}

@Composable
fun TecnicoBody(onSaveTecnico: (TecnicoEntity) -> Unit) {
    val regex = Regex("[0-9]*\\.?[0-9]*")
    var tecnicoId by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var sueldoHora by remember { mutableDoubleStateOf(0.0) }
    var showDiagSaveError by remember { mutableStateOf(false) }


    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {


            OutlinedTextField(
                label = { Text(text = "Nombres") },
                value = nombres,
                onValueChange = { nombres = it },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                label = { Text(text = "Sueldo por hora") },
                value = sueldoHora.toString(),
                onValueChange = {if (it.matches(regex)) {
                    sueldoHora = it.toDoubleOrNull() ?: 0.0
                }},
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            )

            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    onClick = {
                        tecnicoId = ""
                        nombres = ""
                        sueldoHora = 0.0
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "new button"
                    )
                    Text(text = "Nuevo")
                }
                OutlinedButton(
                    onClick = {

                        if (ValidarTecnico(nombres, sueldoHora))
                        {
                            showDiagSaveError = true
                            return@OutlinedButton
                        }
                        onSaveTecnico(
                            TecnicoEntity(
                                tecnicoId = tecnicoId.toIntOrNull(),
                                nombres = nombres,
                                sueldoHora = sueldoHora
                            )
                        )
                        tecnicoId = ""
                        nombres = ""
                        sueldoHora = 0.0
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "save button"
                    )
                    Text(text = "Guardar")
                }

                if (showDiagSaveError) {

                    AlertDialog(
                        onDismissRequest = { showDiagSaveError = false },
                        title = { Text("Error al guardar") },
                        text = { Text(mensajeDeValidacion(nombres, sueldoHora)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDiagSaveError = false
                                }
                            ) {
                                Text("Aceptar")
                            }
                        }
                    )
                }

            }
        }

    }

}


private fun ValidarTecnico(nombres: String, sueldoHora: Double): Boolean {
    if (nombres.isEmpty()) {
        return true
    }
    if (ValidarNombreSoloCaracteres(nombres)) {
        return true
    }
    if (sueldoHora <= 0) {
        return true
    }
    return false
}

private fun ValidarNombreSoloCaracteres(nombre: String): Boolean {
    val regex = Regex("^[a-zA-Z ]+\$")
    if (!regex.matches(nombre)) {

        return true
    }
    return false
}

private fun mensajeDeValidacion(nombres: String, SueldoHora: Double): String{
    var mensaje = ""
    if (nombres.isEmpty()) {
        mensaje += "Debe ingresar un nombre"
    }
    if (ValidarNombreSoloCaracteres(nombres)) {
        mensaje += "\nEl nombre solo puede contener letras"
    }
    if (SueldoHora <= 0) {
        mensaje += "\nEl sueldo por hora debe ser mayor a 0"
    }
    return mensaje
}

@Preview
@Composable
private fun TecnicoPreview() {
    TecnicosAppTheme {
        TecnicoBody() {
        }
    }
}