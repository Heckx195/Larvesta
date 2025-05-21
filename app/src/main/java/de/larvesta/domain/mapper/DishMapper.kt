package de.larvesta.domain.mapper

import de.larvesta.data.model.DishEntity
import de.larvesta.domain.model.Dish

// Convert DishEntity to Dish
fun DishEntity.toDomain(): Dish {
    return Dish(
        id = this.id,
        name = this.name,
        foods = this.foods.map { it.toDomain() },
        lastUsed = this.lastUsed,
        nutriments = this.nutriments,
    )
}

// Convert Dish to DishEntity
fun Dish.toEntity(): DishEntity {
    return DishEntity(
        id = this.id,
        name = this.name,
        foods = this.foods.map { it.toEntity() },
        lastUsed = this.lastUsed,
        nutriments = this.nutriments,
    )
}