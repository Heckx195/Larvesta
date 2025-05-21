package de.larvesta.presentation.ui.components.food

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
import de.larvesta.domain.model.Nutriments
import java.util.Date

@Composable
fun AddFoodButton(
    foodViewModel: FoodViewModel
) {
    var showDialog by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val barcode by remember { mutableStateOf("") } // TODO: remove?

    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbohydrates by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Add Food")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Add Food") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        value = calories,
                        onValueChange = { calories = it },
                        label = { Text("Calories / 100g") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        value = protein,
                        onValueChange = { protein = it },
                        label = { Text("Protein / 100g") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        value = carbohydrates,
                        onValueChange = { carbohydrates = it },
                        label = { Text("Carbohydrates / 100g") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    OutlinedTextField(
                        value = fats,
                        onValueChange = { fats = it },
                        label = { Text("Fats / 100g") },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newFood = Food (
                            id = (0..1000).random(), // Random id number
                            name = name,
                            category = category,
                            barcode = barcode,
                            lastUsed = Date(0L),
                            nutriments = Nutriments(
                                calories = calories.toDoubleOrNull() ?: 0.0,
                                protein = protein.toDoubleOrNull() ?: 0.0,
                                carbohydrates = carbohydrates.toDoubleOrNull() ?: 0.0,
                                fats = fats.toDoubleOrNull() ?: 0.0
                            )
                        )
                        foodViewModel.addFood(newFood)
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