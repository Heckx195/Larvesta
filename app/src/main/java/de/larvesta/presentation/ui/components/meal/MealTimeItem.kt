package de.larvesta.presentation.ui.components.meal

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.painter.Painter
import androidx.navigation.NavHostController

@Composable
fun MealTimeItem(
    title: String,
    description: String,
    image: Painter,
    navController: NavHostController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = image,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp)
                .padding(end = 16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = { navController.navigate("mealAdder/$title") }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Meal",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}