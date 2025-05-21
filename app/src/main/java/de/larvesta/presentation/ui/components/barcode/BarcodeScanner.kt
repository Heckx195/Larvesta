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

    var foodDetails by remember { mutableStateOf("Scan a barcode to get food details") }
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

    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    var scannedProduct by remember { mutableStateOf<Product?>(null) }
    val fetchedFood by foodViewModel.food.collectAsState()

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
            text = foodDetails,
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
                                scannedBarcode = barcode
                                scannedProduct = product
                                foodViewModel.fetchFoodByBarcode(barcode)
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

        LaunchedEffect(fetchedFood) {
            val barcode = scannedBarcode
            val product = scannedProduct
            if (barcode.isNullOrEmpty() || product == null || product.product_name == null) return@LaunchedEffect

            Log.e("BarcodeScanner fetchedFood", fetchedFood.toString())

            if (fetchedFood == null) {
                val newFood = Food(
                    id = (0..1000).random(),
                    name = product.product_name ?: "Unknown",
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
                foodViewModel.addFood(newFood)
                foodDetails = "Added new food: ${newFood.name} (barcode: $barcode)"

                if (!selectedMeals.contains(newFood.name)) {
                    selectedMeals.add(newFood.name)
                }

                navController.previousBackStackEntry?.arguments?.putStringArrayList(
                    "selectedMeals",
                    ArrayList(selectedMeals)
                )
                navController.popBackStack()

                //navController.navigate("mealAdder/Breakfast?addedFood=${newFood.name}")
            } else {
                foodDetails = "Fetched product: ${product.product_name} (barcode: $barcode)"

                if (!selectedMeals.contains(product.product_name)) {
                    selectedMeals.add(product.product_name)
                }

                navController.previousBackStackEntry?.arguments?.putStringArrayList(
                    "selectedMeals",
                    ArrayList(selectedMeals)
                )
                navController.popBackStack()

                //navController.navigate("mealAdder/Breakfast?addedFood=${product.product_name}")
            }
            scannedBarcode = null
            scannedProduct = null
        }
    }
}