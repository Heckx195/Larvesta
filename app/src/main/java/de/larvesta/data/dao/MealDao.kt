package de.larvesta.data.dao

import androidx.room.*
import de.larvesta.data.model.MealEntity

@Dao
interface MealDao {
    @Insert
    suspend fun insert(meal: MealEntity)

    @Update
    suspend fun update(meal: MealEntity)

    @Query("SELECT * FROM meal_table")
    suspend fun getAllMeals(): List<MealEntity>

    @Query("SELECT * FROM meal_table WHERE id = :id")
    suspend fun getMealById(id: Int): MealEntity?

    @Delete
    suspend fun delete(meal: MealEntity)
}

// Info: contain the methods to interact with the database.
