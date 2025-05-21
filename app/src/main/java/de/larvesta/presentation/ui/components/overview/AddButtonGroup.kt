package de.larvesta.presentation.ui.components.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun AddButtonGroup(
    onAddDishClick: () -> Unit,
    onAddFoodClick: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.width(8.dp))
            ExtendedFloatingActionButton(
                text = { Text("Add Dish") },
                onClick = onAddDishClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Dish",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                modifier = Modifier.padding(end = 8.dp)
            )
            ExtendedFloatingActionButton(
                text = { Text("Add Food") },
                onClick = onAddFoodClick,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Food",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        }
    }
}