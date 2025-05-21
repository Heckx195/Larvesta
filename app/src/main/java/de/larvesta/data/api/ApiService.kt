package de.larvesta.data.api

import de.larvesta.data.model.FoodResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call

// For OpenFoodFacts API
interface ApiService {
    @GET("api/v2/product/{barcode}?fields=product_name,nutriments") // v0
    fun getFoodDetails(@Path("barcode") barcode: String): Call<FoodResponse>
}