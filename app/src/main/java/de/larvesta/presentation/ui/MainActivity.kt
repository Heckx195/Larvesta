package de.larvesta.presentation.ui

import de.larvesta.presentation.ui.components.MyApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import de.larvesta.presentation.ui.theme.LarvestaTheme
import de.larvesta.presentation.viewmodel.DayViewModel
import de.larvesta.presentation.viewmodel.DishViewModel
import de.larvesta.presentation.viewmodel.FoodViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService

    private val dishViewModel: DishViewModel by viewModel()
    private val foodViewModel: FoodViewModel by viewModel()
    private val dayViewModel: DayViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {
            LarvestaTheme {
                MyApp(
                    cameraExecutor,
                    dishViewModel,
                    foodViewModel,
                    dayViewModel
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}