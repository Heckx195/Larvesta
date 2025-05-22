package de.larvesta.presentation.ui.components.day

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import de.larvesta.R
import de.larvesta.domain.model.Nutriments
import de.larvesta.presentation.ui.components.meal.MealTimeItem
import java.time.LocalDate
import java.util.Locale

@Composable
fun DayOverview(
    date: LocalDate,
    eaten: Nutriments,
    target: Nutriments,
    navController: NavHostController,
) {
    val remainingCalories = target.calories - eaten.calories
    val calorieProgress = eaten.calories.toFloat() / target.calories

    val primaryColor = MaterialTheme.colorScheme.primary

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Status bar calories
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(200.dp)
            ) {
                Canvas(modifier = Modifier.size(200.dp)) {
                    drawCircle(
                        color = Color.LightGray,
                        style = Stroke(width = 20f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = (360 * calorieProgress).toFloat(),
                        useCenter = false,
                        style = Stroke(width = 20f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$remainingCalories kcal",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "left",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = date.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        item {
            val carbohydratesProgress = eaten.carbohydrates.toFloat() / target.carbohydrates.toFloat()
            NutrientStatusBar(
                "Carbohydrates: ${String.format(Locale.ROOT, "%.1f", eaten.carbohydrates)} / ${String.format(Locale.ROOT, "%.1f", target.carbohydrates)}",
                carbohydratesProgress
            )

            val proteinProgress = eaten.protein.toFloat() / target.protein.toFloat()
            NutrientStatusBar(
                "Protein: ${String.format(Locale.ROOT, "%.1f", eaten.protein)} / ${String.format(Locale.ROOT, "%.1f", target.protein)}",
                proteinProgress
            )

            val fatProgress = eaten.fats.toFloat() / target.fats.toFloat()
            NutrientStatusBar(
                "Fats: ${String.format(Locale.ROOT, "%.1f", eaten.fats)} / ${String.format(Locale.ROOT, "%.1f", target.fats)}",
                fatProgress
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        items(listOf(
            Triple("Breakfast", "Recommended 535 - 802 kcal", R.drawable.breakfast),
            Triple("Lunch", "Recommended 802 - 1070 kcal", R.drawable.lunch),
            Triple("Dinner", "Recommended 802 - 1070 kcal", R.drawable.dinner),
            Triple("Snack", "Recommended 134 - 267 kcal", R.drawable.snack)
        )) { meal ->
            MealTimeItem(
                title = meal.first,
                description = meal.second,
                image = painterResource(id = meal.third),
                navController = navController
            )
        }
    }
}