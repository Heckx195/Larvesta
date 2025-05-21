package de.larvesta.domain.repository

import de.larvesta.data.dao.MealDao
import de.larvesta.domain.mapper.toDomain
import de.larvesta.domain.mapper.toEntity
import de.larvesta.domain.model.Meal

class MealRepository(private val mealDao: MealDao) {
    suspend fun insertMeal(meal: Meal) {
        mealDao.insert(meal.toEntity())
    }

    suspend fun getMeals(): List<Meal> {
        return mealDao.getAllMeals().map { it.toDomain() }
    }

    suspend fun getMealById(id: Int): Meal? {
        val mealEntity = mealDao.getMealById(id)
        return mealEntity?.toDomain()
    }

    suspend fun deleteMeal(meal: Meal) {
        mealDao.delete(meal.toEntity())
    }

    suspend fun updateMeal(meal: Meal) {
        mealDao.update(meal.toEntity())
    }
}