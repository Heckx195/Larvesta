package de.larvesta.domain.model

import java.util.Date

data class Dish(
    val id: Int,
    var name: String,
    var foods: List<Food>,
    var nutriments: Nutriments,
    var lastUsed: Date,
)