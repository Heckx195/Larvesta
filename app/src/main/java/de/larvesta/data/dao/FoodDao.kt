package de.larvesta.data.dao

import androidx.room.*
import de.larvesta.data.model.FoodEntity

@Dao
interface FoodDao {
    @Insert
    suspend fun insert(food: FoodEntity)

    @Update
    suspend fun update(food: FoodEntity)

    @Query("SELECT * FROM food_table")
    suspend fun getAllFoods(): List<FoodEntity>

    @Query("SELECT * FROM food_table WHERE id = :id")
    suspend fun getFoodById(id: Int): FoodEntity?

    @Query("SELECT * FROM food_table WHERE barcode = :barcode")
    suspend fun getFoodByBarcode(barcode: String): FoodEntity?

    @Delete
    suspend fun delete(food: FoodEntity)
}

// Info: contain the methods to interact with the database.