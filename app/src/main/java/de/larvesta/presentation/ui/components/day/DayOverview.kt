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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import de.larvesta.R
import de.larvesta.domain.model.Nutriments
import de.larvesta.presentation.ui.components.meal.MealTimeItem
import de.larvesta.presentation.viewmodel.DayViewModel
import java.time.LocalDate
import java.util.Locale

@Composable
fun DayOverview(
    dayViewModel: DayViewModel,
    navController: NavHostController,
) {
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    var currentDate by remember {
        mutableStateOf(savedStateHandle?.get<LocalDate>("currentDate") ?: LocalDate.now())
    }

    val currentDay by dayViewModel.day.collectAsState()

    LaunchedEffect(currentDate) {
        savedStateHandle?.set("currentDate", currentDate)
        val fetchedDay = dayViewModel.fetchDayByDateSuspend(currentDate)
        if (fetchedDay == null) {
            val newDay = de.larvesta.domain.model.Day(
                id = 0,
                date = currentDate,
                nutriments = Nutriments(0.0, 0.0, 0.0, 0.0),
                target = Nutriments(2200.0, 72.0, 52.0, 320.0),
                breakfast = emptyList(),
                lunch = emptyList(),
                dinner = emptyList(),
                snack = emptyList()
            )
            dayViewModel.addDay(newDay)
        }
        dayViewModel.fetchDayByDateSuspend(currentDate)
    }

    val defaultNutriments = Nutriments(calories = 0.0, carbohydrates = 0.0, protein = 0.0, fats = 0.0)
    val defaultTarget = Nutriments(calories = 1800.0, carbohydrates = 200.0, protein = 20.0, fats = 20.0)
    val defaultDate = LocalDate.parse("1970-01-01")

    val date = currentDay?.date ?: defaultDate
    val eaten = currentDay?.nutriments ?: defaultNutriments
    val target = currentDay?.target ?: defaultTarget




    // ----------------------------

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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous Day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { currentDate = currentDate.minusDays(1) }
                )

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

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next Day",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { currentDate = currentDate.plusDays(1) }
                )
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
                currentDateRaw = currentDate.toString(),
                image = painterResource(id = meal.third),
                navController = navController
            )
        }
    }
}