package de.larvesta.presentation.ui.components.food

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.larvesta.domain.model.Food

@Composable
fun FoodEntry(
    food: Food,
    onClick: () -> Unit,
) {
    Card(
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = food.name,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(text = food.nutriments.calories.toString() + " kcal")
        }
    }
}