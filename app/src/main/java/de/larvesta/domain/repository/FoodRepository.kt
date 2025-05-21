package de.larvesta.domain.repository

import de.larvesta.data.dao.FoodDao
import de.larvesta.domain.mapper.toDomain
import de.larvesta.domain.mapper.toEntity
import de.larvesta.domain.model.Food

class FoodRepository(private val foodDao: FoodDao) {
    suspend fun insertFood(food: Food) {
        foodDao.insert(food.toEntity())
    }

    suspend fun getFoods(): List<Food> {
        return foodDao.getAllFoods().map { it.toDomain() }
    }

    suspend fun getFoodById(id: Int): Food? {
        val foodEntity = foodDao.getFoodById(id)
        return foodEntity?.toDomain()
    }

    suspend fun getFoodByBarcode(barcode: String): Food? {
        val foodEntity = foodDao.getFoodByBarcode(barcode)
        return foodEntity?.toDomain()
    }

    suspend fun deleteFood(food: Food) {
        foodDao.delete(food.toEntity())
    }

    suspend fun updateFood(food: Food) {
        foodDao.update(food.toEntity())
    }
}