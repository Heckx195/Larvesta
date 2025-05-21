package de.larvesta.domain.model

import java.util.Date

data class Food(
    val id: Int,
    var name: String,
    var barcode: String,
    var nutriments: Nutriments,
    var lastUsed: Date,
    var category: String,
)


