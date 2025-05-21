package de.larvesta.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.larvesta.domain.repository.DishRepository
import de.larvesta.domain.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DishViewModel(private val repository: DishRepository) : ViewModel() {
    private val _dishes = MutableStateFlow<List<Dish>>(emptyList())
    val dishes: StateFlow<List<Dish>> get() = _dishes

    private val _dish = MutableStateFlow<Dish?>(null)
    val dish: StateFlow<Dish?> get() = _dish

    // Automatically fetch all dishes when the ViewModel is initialized
    init {
        fetchDishes()
    }

    fun fetchDishes() {
        viewModelScope.launch {
            val fetchedDishes = repository.getDishes()
            _dishes.value = fetchedDishes
        }
    }

    fun addDish (dish: Dish) {
        viewModelScope.launch {
            repository.insertDish(dish)
            fetchDishes()
        }
    }

    fun updateDish(dish: Dish) {
        viewModelScope.launch {
            repository.updateDish(dish)
        }
    }

    fun deleteDish(dish: Dish) {
        viewModelScope.launch {
            repository.deleteDish(dish)
            fetchDishes()
        }
    }

    fun fetchDishById(id: Int) {
        viewModelScope.launch {
            val fetchedDish = repository.getDishById(id)
            _dish.value = fetchedDish
        }
    }
}