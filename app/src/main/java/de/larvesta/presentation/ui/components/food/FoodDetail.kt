package de.larvesta.presentation.ui.components.food

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.larvesta.domain.model.Food
import de.larvesta.presentation.ui.components.common.GoBackButton

@Composable
fun FoodDetail(
    food: Food,
    navController: NavHostController
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .height(56.dp)
            ) {
                GoBackButton(
                    onClick = { navController.popBackStack() },
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = food.name,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            HorizontalDivider(thickness = 2.dp)

            // Circle status bars
            /*
            val carbohydratesProgress = eaten.carbohydrates.toFloat() / target.carbohydrates.toFloat()
            NutrientStatusBar("Carbohydrates", carbohydratesProgress)

            val proteinProgress = eaten.protein.toFloat() / target.protein.toFloat()
            NutrientStatusBar("Protein", proteinProgress)

            val fatProgress = eaten.fats.toFloat() / target.fats.toFloat()
            NutrientStatusBar("Fats", fatProgress)
             */

            Text(
                "Calories: " + food.nutriments.calories
            )
            Text(
                "Protein: " + food.nutriments.protein
            )
            Text(
                "Carbohydrates: " + food.nutriments.carbohydrates
            )
            Text(
                "Fats: " + food.nutriments.fats
            )
        }
    }
}