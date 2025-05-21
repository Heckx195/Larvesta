package de.larvesta.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.larvesta.data.Converters

@Entity(tableName = "meal_table")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @TypeConverters(Converters::class)
    val food: FoodEntity?,
    @TypeConverters(Converters::class)
    val dish: DishEntity?
)

// Info: define the schema for the table in the SQLite database.
