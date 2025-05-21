package de.larvesta.presentation.ui.components.food

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import de.larvesta.domain.model.Food
import de.larvesta.presentation.viewmodel.FoodViewModel
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow

@Composable
fun FoodList(
    foodViewModel: FoodViewModel,
    onFinish: (List<Food>) -> Unit
) {
    val foods = foodViewModel.foods.collectAsState().value.sortedBy { it.name }
    var selectedFoods by remember { mutableStateOf(listOf<Food>()) }

    Column {
        // Watch selectedFoods
        LaunchedEffect(selectedFoods) {
            snapshotFlow { selectedFoods }
                .collect { onFinish(it) }
        }

        LazyColumn {
            item {
                foods.forEach { food ->
                    val isSelected = selectedFoods.contains(food)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedFoods = if (isSelected) {
                                    selectedFoods - food
                                } else {
                                    selectedFoods + food
                                }
                            },
                        border = if (isSelected) {
                            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                        } else {
                            null
                        }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = food.name, style = MaterialTheme.typography.bodyLarge)
                            Text(text = "${food.nutriments.calories} kcal", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}