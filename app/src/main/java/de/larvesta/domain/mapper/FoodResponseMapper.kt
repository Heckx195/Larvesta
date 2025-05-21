package de.larvesta.domain.mapper

import de.larvesta.data.model.FoodResponse
import de.larvesta.domain.model.Food
import de.larvesta.domain.model.Nutriments
import java.util.Date

fun FoodResponse.toDomainModel(): Food? {
    val product = this.product ?: return null
    val nutriments = product.nutriments ?: return null

    return Food(
        id = 0, // ID wird später in der Datenbank generiert
        name = product.product_name ?: "Unknown",
        barcode = "", // Barcode ist nicht in der API enthalten, kann aber ergänzt werden
        category = "Unknown", // Kategorie ist nicht in der API enthalten
        lastUsed = Date(0L),
        nutriments = Nutriments(
            calories = nutriments.energy_kcal ?: 0.0,
            protein = nutriments.proteins ?: 0.0,
            carbohydrates = nutriments.carbohydrates ?: 0.0,
            fats = nutriments.fat ?: 0.0
        )
    )
}