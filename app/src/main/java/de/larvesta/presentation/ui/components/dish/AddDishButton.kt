package de.larvesta.presentation.ui.components.dish

import de.larvesta.presentation.viewmodel.FoodViewModel
import de.larvesta.domain.model.Food
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.larvesta.domain.model.Dish
import de.larvesta.domain.model.Nutriments
import de.larvesta.presentation.ui.components.food.FoodList
import de.larvesta.presentation.viewmodel.DishViewModel
import java.util.Date

@Composable
fun AddDishButton(
    dishViewModel: DishViewModel,
    foodViewModel: FoodViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var foods by remember { mutableStateOf(listOf<Food>()) }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Add Dish")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Add Dish") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    FoodList(
                        foodViewModel = foodViewModel,
                        onFinish = { selectedFoods -> foods = selectedFoods }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newDish = Dish(
                            id = (0..1000).random(),
                            name = name,
                            foods = foods,
                            lastUsed = Date(),
                            nutriments = Nutriments(
                                calories = foods.sumOf { it.nutriments.calories },
                                protein = foods.sumOf { it.nutriments.protein },
                                carbohydrates = foods.sumOf { it.nutriments.carbohydrates },
                                fats = foods.sumOf { it.nutriments.fats }
                            )
                        )
                        dishViewModel.addDish(newDish)
                        showDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}