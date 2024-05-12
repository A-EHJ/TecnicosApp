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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ucne.myapplication.data.local.entities.TecnicoEntity





@Composable
fun TecnicoScreen(
    viewModel: TecnicoViewModel
) {
    val tecnicos by viewModel.tecnicos.collectAsStateWithLifecycle()
    TecnicoBody(
        tecnicos = tecnicos,
        onSaveTecnico = { tecnico ->
            viewModel.saveTecnico(tecnico)
        }
    )
}

@Composable
fun TecnicoBody(tecnicos: List<TecnicoEntity>,onSaveTecnico: (TecnicoEntity) -> Unit) {
    val regexSueldo = Regex("[0-9]*\\.?[0-9]*")
    val regexNombre = Regex("^[a-zA-Z ]+\$")
    var tecnicoId by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var sueldoHora by remember { mutableDoubleStateOf(0.0) }
    var nombreValido by remember { mutableStateOf(true) }
    var sueldoValido by remember { mutableStateOf(true) }
    var nombreExiste by remember { mutableStateOf(false) }

    val context = LocalContext.current
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
                onValueChange = {if (it.matches(regexNombre) || it.isEmpty()) {
                    nombres = it
                }},
                isError = !nombreValido || nombreExiste,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )


            if (!nombreValido) {
                Text(text = "Debe agregar un nombre", color = Color.Red)
                }
            if (nombreExiste) {
                Text(text = "El nombre ya existe", color = Color.Red)
            }


            OutlinedTextField(
                label = { Text(text = "Sueldo por hora") },
                value = sueldoHora.toString(),
                onValueChange = {if (it.matches(regexSueldo) ) {
                    sueldoHora = it.toDoubleOrNull() ?: 0.0
                }},
                isError = !nombreValido,
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            )

            if (!sueldoValido) {
                Text(text = "El sueldo debe ser mayor a 0", color = Color.Red)
            }

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
                        nombreValido = true
                        sueldoValido = true
                        nombreExiste = false
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

                        val (tecnicoEsValido, nombreValidoResult, sueldoValidoResult) = validarTecnico(nombres, sueldoHora)

                        if (!tecnicoEsValido) {
                            nombreValido = nombreValidoResult
                            sueldoValido = sueldoValidoResult
                            return@OutlinedButton
                        }

                        if (existeNombreTecnico(nombres, tecnicos)) {
                            nombreExiste = true
                            return@OutlinedButton
                        }
                        else
                            nombreExiste = false

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
                        nombreValido = true
                        sueldoValido = true
                        nombreExiste = false
                        Toast.makeText(context, "Tecnico agregado", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "save button"
                    )
                    Text(text = "Guardar")
                }


            }

        }

    }

}


private fun validarTecnico(nombres: String, sueldoHora: Double): Triple<Boolean, Boolean, Boolean> {
    var tecnicoEsValido = true
    val nombreValidoResult: Boolean
    val sueldoValidoResult: Boolean

    if (nombres.isEmpty()) {
        nombreValidoResult = false
        tecnicoEsValido = false
    } else {
        nombreValidoResult = true
    }

    if (tecnicoEsValido && sonLetrasYEspacios(nombres)) {
        tecnicoEsValido = false
    }
    if (sueldoHora <= 0.0) {
        sueldoValidoResult = false
        tecnicoEsValido = false
    } else {
        sueldoValidoResult = true
    }

    return Triple(tecnicoEsValido, nombreValidoResult, sueldoValidoResult)
}



private fun sonLetrasYEspacios(nombre: String): Boolean {
    val regexNombre = Regex("^[a-zA-Z ]+\$")
    return !regexNombre.matches(nombre)
}

private fun existeNombreTecnico (nombre: String, tecnicos: List<TecnicoEntity>): Boolean {
    for (tecnico in tecnicos) {
        if (tecnico.nombres == nombre) {
            return true
        }
    }
    return false
}



