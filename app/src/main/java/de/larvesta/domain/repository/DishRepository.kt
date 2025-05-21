package de.larvesta.domain.repository

import de.larvesta.data.dao.DishDao
import de.larvesta.domain.mapper.toDomain
import de.larvesta.domain.mapper.toEntity
import de.larvesta.domain.model.Dish

class DishRepository(private val dishDao: DishDao) {
    suspend fun insertDish(dish: Dish) {
        dishDao.insert(dish.toEntity())
    }

    suspend fun getDishes(): List<Dish> {
        return dishDao.getAllDishs().map { it.toDomain() }
    }

    suspend fun getDishById(id: Int): Dish? {
        val dishEntity = dishDao.getDishById(id)
        return dishEntity?.toDomain()
    }

    suspend fun deleteDish(dish: Dish) {
        dishDao.delete(dish.toEntity())
    }

    suspend fun updateDish(dish: Dish) {
        dishDao.update(dish.toEntity())
    }
}

// Info:    fetch data from the database.
//          Use mappers to map toDomain DishDao or to Entity Dish