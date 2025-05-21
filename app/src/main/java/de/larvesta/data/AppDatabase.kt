package de.larvesta.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.larvesta.data.dao.DayDao
import de.larvesta.data.dao.DishDao
import de.larvesta.data.dao.FoodDao
import de.larvesta.data.dao.MealDao
import de.larvesta.data.model.DayEntity
import de.larvesta.data.model.DishEntity
import de.larvesta.data.model.FoodEntity
import de.larvesta.data.model.MealEntity

@Database(
    entities = [
        DishEntity::class,
        FoodEntity::class,
        DayEntity::class,
        MealEntity::class
    ],
    version = 6, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dishDao(): DishDao
    abstract fun foodDao(): FoodDao
    abstract fun dayDao(): DayDao
    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Design Pattern - Singleton
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "larvesta_database"
                )
                    .fallbackToDestructiveMigration() // Only for dev
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Info: defines the Room database with singleton as design pattern.