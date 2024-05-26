package edu.ucne.tecnicosapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import edu.ucne.tecnicosapp.data.local.database.TecnicoDb
import edu.ucne.tecnicosapp.data.repository.TecnicoRepository
import edu.ucne.tecnicosapp.data.repository.TipoTecnicoRepository
import edu.ucne.tecnicosapp.presentation.Component.NavigationDrawer
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoListScreen
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoScreen
import edu.ucne.tecnicosapp.presentation.Tecnico.TecnicoViewModel
import edu.ucne.tecnicosapp.presentation.TipoTecnico.TipoTecnicoListScreen
import edu.ucne.tecnicosapp.presentation.TipoTecnico.TipoTecnicoScreen
import edu.ucne.tecnicosapp.presentation.TipoTecnico.TipoTecnicoViewModel
import edu.ucne.tecnicosapp.ui.theme.TecnicosAppTheme
import kotlinx.serialization.Serializable

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
        val repository2 = TipoTecnicoRepository(tecnicoDb.tipoTecnicoDao())
        enableEdgeToEdge()
        setContent {
            TecnicosAppTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                NavigationDrawer(
                    navController = navController,
                    drawerState = drawerState,
                ) {


                    NavHost(
                        navController = navController,
                        startDestination = Screen.TecnicoList
                    ) {

                        composable<Screen.TecnicoList> {
                            TecnicoListScreen(
                                drawerState = drawerState,
                                viewModel = viewModel {
                                    TecnicoViewModel(
                                        repository,
                                        0,
                                        repository2
                                    )
                                },
                                onVerTecnico = {
                                    navController.navigate(Screen.Tecnico(it.tecnicoId ?: 0))
                                })
                        }

                        composable<Screen.Tecnico> {
                            val args = it.toRoute<Screen.Tecnico>()
                            TecnicoScreen(
                                tecnicoViewModel = viewModel {
                                    TecnicoViewModel(
                                        repository,
                                        args.tecnicoId,
                                        repository2
                                    )
                                },
                                navController = navController
                            )
                        }

                        composable<Screen.TipoTecnicoList> {
                            TipoTecnicoListScreen(
                                drawerState = drawerState,
                                viewModel = viewModel { TipoTecnicoViewModel(repository2, 0) },
                                onVerTipoTecnico = {
                                    navController.navigate(
                                        Screen.TipoTecnico(
                                            it.tipoTecnicoId ?: 0
                                        )
                                    )
                                })
                        }

                        composable<Screen.TipoTecnico> {
                            val args = it.toRoute<Screen.TipoTecnico>()
                            TipoTecnicoScreen(
                                viewModel = viewModel {
                                    TipoTecnicoViewModel(
                                        repository2,
                                        args.tipoTecnicoId
                                    )
                                },
                                navController = navController
                            )
                        }

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
sealed class Screen {
    @Serializable
    object TecnicoList : Screen()

    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen()

    @Serializable
    object TipoTecnicoList : Screen()

    @Serializable
    data class TipoTecnico(val tipoTecnicoId: Int) : Screen()

}
