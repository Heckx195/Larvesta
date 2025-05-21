package de.larvesta.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.larvesta.data.model.DishEntity
import de.larvesta.data.model.FoodEntity
import de.larvesta.domain.model.Meal
import de.larvesta.domain.model.Nutriments
import java.time.LocalDate
import java.util.Date

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromNutriments(nutriments: Nutriments): String {
        return Gson().toJson(nutriments)
    }

    @TypeConverter
    fun toNutriments(nutrimentsString: String): Nutriments {
        val type = object : TypeToken<Nutriments>() {}.type
        return Gson().fromJson(nutrimentsString, type)
    }

    @TypeConverter
    fun fromFoodList(foodList: List<FoodEntity>): String {
        return Gson().toJson(foodList)
    }

    @TypeConverter
    fun toFoodList(foodListString: String): List<FoodEntity> {
        val type = object : TypeToken<List<FoodEntity>>() {}.type
        return Gson().fromJson(foodListString, type)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun toDate(timestamp: Long): Date {
        return Date(timestamp)
    }

    private val gson = Gson()

    @TypeConverter
    fun fromFoodEntity(food: FoodEntity?): String? {
        return food?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toFoodEntity(foodString: String?): FoodEntity? {
        return foodString?.let { gson.fromJson(it, FoodEntity::class.java) }
    }

    @TypeConverter
    fun fromDishEntity(dish: DishEntity?): String? {
        return dish?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toDishEntity(dishString: String?): DishEntity? {
        return dishString?.let { gson.fromJson(it, DishEntity::class.java) }
    }

    @TypeConverter
    fun fromMealList(mealList: List<Meal>): String {
        return gson.toJson(mealList)
    }

    @TypeConverter
    fun toMealList(mealListString: String): List<Meal> {
        val type = object : TypeToken<List<Meal>>() {}.type
        return gson.fromJson(mealListString, type)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString() // Converts in "2023-10-05"
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) } // Convert back to LocalDate
    }
}

// Info: Converter for not supported type in room-database -> List<String>