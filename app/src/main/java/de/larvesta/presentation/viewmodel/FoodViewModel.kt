package de.larvesta.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.larvesta.domain.repository.FoodRepository
import de.larvesta.domain.model.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class FoodViewModel(private val repository: FoodRepository) : ViewModel() {
    private val _foods = MutableStateFlow<List<Food>>(emptyList())
    val foods: StateFlow<List<Food>> get() = _foods

    private val _food = MutableStateFlow<Food?>(null)
    val food: StateFlow<Food?> get() = _food

    // Automatically fetch all dishes when the ViewModel is initialized
    init {
        fetchFoods()
    }

    fun fetchFoods() {
        viewModelScope.launch {
            val fetchedFoods = repository.getFoods()
            _foods.value = fetchedFoods
        }
    }

    fun addFood(food: Food) {
        viewModelScope.launch {
            repository.insertFood(food)
            fetchFoods()
        }
    }

    fun updateFood(food: Food) {
        viewModelScope.launch {
            repository.updateFood(food)
        }
    }

    fun deleteFood(food: Food) {
        viewModelScope.launch {
            repository.deleteFood(food)
            fetchFoods()
        }
    }

    fun fetchFoodById(id: Int) {
        viewModelScope.launch {
            val fetchedFood = repository.getFoodById(id)
            _food.value = fetchedFood
        }
    }

    fun fetchFoodByBarcode(barcode: String) {
        viewModelScope.launch {
            Log.e("FoodViewModel: barcode", barcode)

            val fetchedFood = repository.getFoodByBarcode(barcode)
            Log.e("FoodViewModel: fetchedFood", fetchedFood.toString())

            _food.value = fetchedFood
        }
    }
}