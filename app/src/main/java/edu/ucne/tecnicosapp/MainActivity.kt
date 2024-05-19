package edu.ucne.tecnicosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import edu.ucne.tecnicosapp.data.local.database.TipoTecnicoDb
import edu.ucne.tecnicosapp.data.local.database.TecnicoDb
import edu.ucne.tecnicosapp.data.repository.TecnicoRepository
import edu.ucne.tecnicosapp.data.repository.TipoTecnicoRepository
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoListScreen
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoScreen
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoViewModel
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoDb: TecnicoDb
    private lateinit var tipoTecnicoDb: TipoTecnicoDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tecnicoDb = Room.databaseBuilder(
            this,
            TecnicoDb::class.java,
            "Tecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()
        tipoTecnicoDb = Room.databaseBuilder(
            this,
            TipoTecnicoDb::class.java,
            "TipoTecnico.db"
        )
            .fallbackToDestructiveMigration()
            .build()

        val repository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val repository2 = TipoTecnicoRepository(tipoTecnicoDb.TipoTecnicoDao())
        enableEdgeToEdge()
        setContent {
            TecnicosAppTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.TecnicoList) {

                    composable<Screen.TecnicoList> {
                        TecnicoListScreen(
                            viewModel = viewModel { TecnicoViewModel(repository,0) },
                            onVerTecnico = {
                                navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                            })
                    }

                    composable<Screen.Tecnico> {
                        val args = it.toRoute<Screen.Tecnico>()
                        TecnicoScreen(viewModel = viewModel { TecnicoViewModel(repository,args.tecnicoId) },
                        navController = navController)
                    }
                }

            }
        }
    }
}
/*
               TecnicoScreen(viewModel = viewModel {
                            TecnicoViewModel.provideFactory(repository)

                        })



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
                }*/
sealed class Screen() {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

}