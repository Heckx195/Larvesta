package de.larvesta.data.dao

import androidx.room.*
import de.larvesta.data.model.DishEntity

@Dao
interface DishDao {
    @Insert
    suspend fun insert(dish: DishEntity)

    @Update
    suspend fun update(dish: DishEntity)

    @Query("SELECT * FROM dish_table")
    suspend fun getAllDishs(): List<DishEntity>

    @Query("SELECT * FROM dish_table WHERE id = :id")
    suspend fun getDishById(id: Int): DishEntity?

    @Delete
    suspend fun delete(dish: DishEntity)
}

// Info: contain the methods to interact with the database.