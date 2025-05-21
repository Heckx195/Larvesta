package de.larvesta.domain.mapper

import de.larvesta.data.model.FoodEntity
import de.larvesta.domain.model.Food

// Convert FoodEntity to Food
fun FoodEntity.toDomain(): Food {
    return Food(
        id = this.id,
        name = this.name,
        barcode = this.barcode,
        nutriments = this.nutriments,
        lastUsed = this.lastUsed,
        category = this.category,
    )
}

// Convert Food to FoodEntity
fun Food.toEntity(): FoodEntity {
    return FoodEntity(
        id = this.id,
        name = this.name,
        barcode = this.barcode,
        nutriments = this.nutriments,
        lastUsed = this.lastUsed,
        category = this.category,
    )
}