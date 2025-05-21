package de.larvesta.domain.barcode

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import de.larvesta.data.api.ApiService
import de.larvesta.data.model.FoodResponse
import de.larvesta.data.model.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class BarcodeAnalyzer(
    private val onProductFetched: (Pair<Product?, String>) -> Unit
) : ImageAnalysis.Analyzer {
    private val scanner: BarcodeScanner = BarcodeScanning.getClient()
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val isProcessing = AtomicBoolean(false)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(
        imageProxy: ImageProxy
    ) {
        if (isProcessing.get()) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val barcodeValue = barcode.displayValue
                        if (!barcodeValue.isNullOrEmpty() && isProcessing.compareAndSet(false, true)) {
                            fetchProductDetails(barcodeValue)
                            break
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("BarcodeAnalyzer", "Barcode detection failed", it)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun fetchProductDetails(
        barcode: String
    ) {
        apiService.getFoodDetails(barcode).enqueue(object : Callback<FoodResponse> {
            override fun onResponse(
                call: Call<FoodResponse>,
                response: Response<FoodResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val product = response.body()!!.product
                    onProductFetched(Pair(product, barcode))
                } else {
                    onProductFetched(Pair(null, barcode))
                }
                isProcessing.set(false)
            }

            override fun onFailure(call: Call<FoodResponse>, t: Throwable) {
                Log.e("BarcodeAnalyzer", "API call failed", t)
                onProductFetched(Pair(null, barcode))
                isProcessing.set(false)
            }
        })
    }
}