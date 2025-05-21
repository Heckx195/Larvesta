package de.larvesta.presentation.ui.components.barcode

import android.Manifest
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.ExecutorService
import androidx.camera.view.PreviewView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.Preview
import androidx.navigation.NavHostController
import de.larvesta.data.model.Product
import de.larvesta.domain.model.Food
import de.larvesta.domain.model.Nutriments
import de.larvesta.domain.barcode.BarcodeAnalyzer
import de.larvesta.presentation.ui.components.common.GoBackButton
import de.larvesta.presentation.viewmodel.FoodViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScanner(
    navController: NavHostController,
    cameraExecutor: ExecutorService,
    foodViewModel: FoodViewModel
) {
    val selectedMeals = navController.previousBackStackEntry
        ?.arguments
        ?.getStringArrayList("selectedMeals")
        ?.toMutableList() ?: mutableListOf()

    val foodDetails = remember { mutableStateOf("Scan a barcode to get food details") }
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val lifecycleOwner = LocalLifecycleOwner.current // <-- HIER speichern!

    // Permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            title = { Text(text = "Barcode Scanner", style = MaterialTheme.typography.titleLarge) },
            navigationIcon = { GoBackButton(onClick = { navController.popBackStack() }) }
        )

        val previewView = remember { PreviewView(context) }
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Text(
            text = foodDetails.value,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // CameraX-Setup
        LaunchedEffect(cameraProviderFuture) {
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1280, 720))
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { (product, barcode) ->
                            if (product != null && barcode.isNotEmpty()) {

                                CoroutineScope(Dispatchers.Main).launch {
                                    handleBarcodeResult(
                                        barcode = barcode,
                                        product = product,
                                        foodDetails = foodDetails,
                                        selectedMeals = selectedMeals,
                                        foodViewModel = foodViewModel,
                                        navController = navController
                                    )
                                }
                            }
                        })
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            }, ContextCompat.getMainExecutor(context))
        }
    }
}

private suspend fun handleBarcodeResult(
    barcode: String,
    product: Product?,
    foodDetails: MutableState<String>,
    selectedMeals: MutableList<String>? = null,
    foodViewModel: FoodViewModel,
    navController: NavHostController
) {
    if (barcode.isEmpty() || product == null || product.product_name == null) return

    foodViewModel.fetchFoodByBarcodeSuspend(barcode)
    val fetchedFood = foodViewModel.food.value

    Log.e("BarcodeScanner fetchedFood", fetchedFood.toString())

    if (fetchedFood == null) {
        val newFood = Food(
            id = (0..1000).random(),
            name = product.product_name,
            barcode = barcode,
            category = "BarcodeAdded",
            lastUsed = Date(),
            nutriments = Nutriments(
                calories = product.nutriments?.energy_kcal ?: 4.0,
                protein = product.nutriments?.proteins ?: 4.0,
                carbohydrates = product.nutriments?.carbohydrates ?: 4.0,
                fats = product.nutriments?.fat ?: 4.0
            )
        )
        // TODO: Check if food already exist -> safety thing
        foodViewModel.addFood(newFood)
        foodDetails.value = "Added new food: ${newFood.name} (barcode: $barcode)"

        selectedMeals?.let {
            if (!it.contains(newFood.name)) {
                it.add(newFood.name)
            }
        }
    } else {
        foodDetails.value = "Fetched product: ${product.product_name} (barcode: $barcode)"

        selectedMeals?.let {
            if (!it.contains(product.product_name)) {
                it.add(product.product_name)
            }
        }
    }

    navController.previousBackStackEntry?.arguments?.putStringArrayList(
        "selectedMeals",
        selectedMeals?.let { ArrayList(it) }
    )
    navController.popBackStack()
}