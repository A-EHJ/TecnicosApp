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
import edu.ucne.tecnicosapp.data.repository.ServicioRepository
import edu.ucne.tecnicosapp.data.repository.TecnicoRepository
import edu.ucne.tecnicosapp.data.repository.TipoTecnicoRepository
import edu.ucne.tecnicosapp.presentation.Component.NavigationDrawer
import edu.ucne.tecnicosapp.presentation.Servicio.ServicioListScreen
import edu.ucne.tecnicosapp.presentation.Servicio.ServicioScreen
import edu.ucne.tecnicosapp.presentation.Servicio.ServicioViewModel
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

        val tecnicoRepository = TecnicoRepository(tecnicoDb.tecnicoDao())
        val tipoTecnicoTepository = TipoTecnicoRepository(tecnicoDb.tipoTecnicoDao())
        val servicioRepository = ServicioRepository(tecnicoDb.servicioDao())

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
                                        tecnicoRepository,
                                        0,
                                        tipoTecnicoTepository
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
                                        tecnicoRepository,
                                        args.tecnicoId,
                                        tipoTecnicoTepository
                                    )
                                },
                                navController = navController
                            )
                        }

                        composable<Screen.TipoTecnicoList> {
                            TipoTecnicoListScreen(
                                drawerState = drawerState,
                                viewModel = viewModel { TipoTecnicoViewModel(tipoTecnicoTepository, 0) },
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
                                        tipoTecnicoTepository,
                                        args.tipoTecnicoId
                                    )
                                },
                                navController = navController
                            )
                        }

                        composable<Screen.ServicioList> {
                            ServicioListScreen(
                                viewModel = viewModel {
                                    ServicioViewModel(
                                        servicioRepository,
                                        0,
                                        tecnicoRepository
                                    )
                                },
                                onVerServicio = {
                                    navController.navigate(Screen.Servicio(it.ServicioId ?: 0))
                                },
                                drawerState = drawerState
                            )
                        }

                        composable<Screen.Servicio> {
                            val args = it.toRoute<Screen.Servicio>()
                            ServicioScreen(
                                viewModel = viewModel {
                                    ServicioViewModel(
                                        servicioRepository,
                                        args.ServicioId,
                                        tecnicoRepository
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


    @Serializable
    object ServicioList : Screen()
    @Serializable
    data class Servicio(val ServicioId: Int) : Screen()


}






