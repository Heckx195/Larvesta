package de.larvesta.domain.mapper

import de.larvesta.data.model.MealEntity
import de.larvesta.domain.model.Meal

// Convert MealEntity to Meal
fun MealEntity.toDomain(): Meal {
    return Meal(
        id = this.id,
        food = this.food?.toDomain(),
        dish = this.dish?.toDomain()
    )
}

// Convert Meal to MealEntity
fun Meal.toEntity(): MealEntity {
    return MealEntity(
        id = this.id,
        food = this.food?.toEntity(),
        dish = this.dish?.toEntity()
    )
}