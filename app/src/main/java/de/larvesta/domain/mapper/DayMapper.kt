package de.larvesta.domain.mapper

import de.larvesta.data.model.DayEntity
import de.larvesta.domain.model.Day

// Convert DayEntity to Day
fun DayEntity.toDomain(): Day {
    return Day(
        id = this.id,
        date = this.date,
        nutriments = this.nutriments,
        target = this.target,
        breakfast = this.breakfast,
        lunch = this.lunch,
        dinner = this.dinner,
        snack = this.snack,
    )
}

// Convert Day to DayEntity
fun Day.toEntity(): DayEntity {
    return DayEntity(
        id = this.id,
        date = this.date,
        nutriments = this.nutriments,
        target = this.target,
        breakfast = this.breakfast,
        lunch = this.lunch,
        dinner = this.dinner,
        snack = this.snack,
    )
}