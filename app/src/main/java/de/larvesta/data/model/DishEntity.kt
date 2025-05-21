package de.larvesta.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.larvesta.data.Converters
import de.larvesta.domain.model.Nutriments
import java.util.Date

@Entity(tableName = "dish_table")
data class DishEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    @TypeConverters(Converters::class)
    val foods: List<FoodEntity>,
    @TypeConverters(Converters::class)
    val lastUsed: Date,
    @TypeConverters(Converters::class)
    val nutriments: Nutriments,
)

// Info: define the schema for the table in the SQLite database.