package edu.ucne.tecnicosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.ucne.myapplication.data.local.database.TecnicoDb
import com.ucne.myapplication.data.repository.TecnicoRepository
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoListScreen
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoScreen
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoViewModel
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        enableEdgeToEdge()
        setContent {
            TecnicosAppTheme {
                Surface {
                    val viewModel: TecnicoViewModel = viewModel(
                        factory = TecnicoViewModel.provideFactory(repository)
                    )
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(8.dp)
                        ) {

                            TecnicoScreen(viewModel = viewModel)
                            TecnicoListScreen(viewModel = viewModel,
                                onVerTecnico = {

                                })
                        }
                    }
                }
            }
        }
    }
}

