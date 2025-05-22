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
    // Parse selectedMeals from navigation arguments
    val backStackEntry = navController.currentBackStackEntry
    val selectedMealsString = backStackEntry?.arguments?.getString("selectedMeals") ?: ""
    val initialMeals = if (selectedMealsString.isNotEmpty()) selectedMealsString.split(",") else emptyList()
    val selectedMeals = remember { mutableStateOf(initialMeals.toMutableList()) }
    Log.e("BarcodeScanner selectedMeals: ", selectedMeals.toString())

    val foodDetails = remember { mutableStateOf("Scan a barcode to get food details") }
    val context = LocalContext.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // State for popBackStack
    var shouldPopBack by remember { mutableStateOf(false) }

    // Permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )
    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // This effect handles navigation in the Compose scope
    LaunchedEffect(shouldPopBack) {
        if (shouldPopBack) {
            Log.e("BarcodeScanner handler: CurrentBackStackEntry: ", navController.currentBackStackEntry?.destination?.route ?: "null")
            Log.e("BarcodeScanner handler: PreviousBackStackEntry: ", navController.previousBackStackEntry?.destination?.route ?: "null")
            navController.popBackStack()
            shouldPopBack = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(
            title = { Text(text = "Barcode Scanner", style = MaterialTheme.typography.titleLarge) },
            navigationIcon = { GoBackButton(onClick = {
                Log.e("BarcodeScanner BackButton: CurrentBackStackEntry: ", navController.currentBackStackEntry?.destination?.route ?: "null")
                Log.e("BarcodeScanner BackButton: PreviousBackStackEntry: ", navController.previousBackStackEntry?.destination?.route ?: "null")
                navController.previousBackStackEntry?.savedStateHandle?.set("selectedMeals", selectedMeals.value.toList())
                navController.popBackStack()
            }) }
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
                                        selectedMeals = selectedMeals.value,
                                        foodViewModel = foodViewModel,
                                        navController = navController,
                                        onScanHandled = { shouldPopBack = true }
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
    navController: NavHostController,
    onScanHandled: () -> Unit
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
        foodViewModel.addFood(newFood)
        foodDetails.value = "Added new food: ${newFood.name} (barcode: $barcode)"

        selectedMeals?.let {
            if (!it.contains(newFood.name)) {
                it.add(newFood.name)
            }
        }
        Log.e("BarcodeScanner selectedMeals: ", selectedMeals.toString())
    } else {
        foodDetails.value = "Fetched product: ${product.product_name} (barcode: $barcode)"

        selectedMeals?.let {
            if (!it.contains(product.product_name)) {
                it.add(product.product_name)
            }
        }
        Log.e("BarcodeScanner selectedMeals: ", selectedMeals.toString())
    }

    // Pass updated selectedMeals back to previous screen via SavedStateHandle
    navController.previousBackStackEntry?.savedStateHandle?.set("selectedMeals", selectedMeals?.toList() ?: emptyList())
    onScanHandled()
}