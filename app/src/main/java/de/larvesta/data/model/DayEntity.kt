package de.larvesta.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.larvesta.data.Converters
import de.larvesta.domain.model.Meal
import de.larvesta.domain.model.Nutriments
import java.time.LocalDate
import java.util.Date

@Entity(
    tableName = "day_table",
    indices = [Index(value = ["date"], unique = true)]
)
data class DayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @TypeConverters(Converters::class)
    val date: LocalDate,
    val nutriments: Nutriments,
    val target: Nutriments,
    @TypeConverters(Converters::class)
    val breakfast: List<Meal>,
    @TypeConverters(Converters::class)
    val lunch: List<Meal>,
    @TypeConverters(Converters::class)
    val dinner: List<Meal>,
    @TypeConverters(Converters::class)
    val snack: List<Meal>,
)

// Info: define the schema for the table in the SQLite database.