package de.larvesta.domain.model

import java.time.LocalDate

data class Day(
    val id: Int,
    var date: LocalDate,

    val nutriments: Nutriments,
    val target: Nutriments,

    var breakfast: List<Meal>,
    var lunch: List<Meal>,
    var dinner: List<Meal>,
    var snack: List<Meal>,
)