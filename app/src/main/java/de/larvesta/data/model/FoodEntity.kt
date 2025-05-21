package de.larvesta.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.larvesta.data.Converters
import de.larvesta.domain.model.Nutriments
import java.util.Date

@Entity(
    tableName = "food_table",
    indices = [Index(value = ["name"], unique = true)]
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val barcode: String,
    @TypeConverters(Converters::class)
    val nutriments: Nutriments,
    @TypeConverters(Converters::class)
    val lastUsed: Date,
    val category: String,
)

// Info: define the schema for the table in the SQLite database.