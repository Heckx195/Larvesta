package de.larvesta.data.model

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    val product: Product?,
    val status: Int,
    val status_verbose: String
)

data class Product(
    val product_name: String?,
    val nutriments: Nutriments?
)

data class Nutriments(
    @SerializedName("energy-kcal") val energy_kcal: Double?,
    val fat: Double?,
    val carbohydrates: Double?,
    val proteins: Double?
)