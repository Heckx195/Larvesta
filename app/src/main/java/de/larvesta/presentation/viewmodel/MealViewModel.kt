package de.larvesta.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.larvesta.domain.repository.MealRepository
import de.larvesta.domain.model.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealViewModel(private val repository: MealRepository) : ViewModel() {
    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> get() = _meals

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> get() = _meal

    init {
        fetchMeals()
    }

    fun fetchMeals() {
        viewModelScope.launch {
            val fetchedMeals = repository.getMeals()
            _meals.value = fetchedMeals
        }
    }

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            repository.insertMeal(meal)
            fetchMeals()
        }
    }

    fun updateMeal(meal: Meal) {
        viewModelScope.launch {
            repository.updateMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repository.deleteMeal(meal)
            fetchMeals()
        }
    }

    fun fetchMealById(id: Int) {
        viewModelScope.launch {
            val fetchedMeal = repository.getMealById(id)
            _meal.value = fetchedMeal
        }
    }
}