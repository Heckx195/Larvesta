package de.larvesta.presentation.ui.components.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import de.larvesta.domain.model.Day
import de.larvesta.domain.model.Nutriments
import de.larvesta.presentation.ui.components.day.DayOverview
import de.larvesta.presentation.viewmodel.DayViewModel
import android.util.Log
import java.time.LocalDate

@Composable
fun OverviewPage(
    navController: NavHostController,
    dayViewModel: DayViewModel,
    modifier: Modifier = Modifier,
) {
    val currentDate = LocalDate.now()

    Log.e("CurrentDate Info:", currentDate.toString())
    LaunchedEffect(currentDate) {
        val fetchedDay = dayViewModel.fetchDayByDateSuspend(currentDate)
        Log.e("LaunchedEffect: FetchedDay:", fetchedDay.toString())

        if (fetchedDay == null) {
            val newDay = Day(
                id = 0, // auto-generated
                date = currentDate,
                nutriments = Nutriments(0.0, 0.0, 0.0, 0.0),
                target = Nutriments(2200.0, 72.0, 52.0, 320.0), // TODO: From Settings-Page
                breakfast = emptyList(),
                lunch = emptyList(),
                dinner = emptyList(),
                snack = emptyList()
            )
            dayViewModel.addDay(newDay)
            dayViewModel.fetchDayByDateSuspend(currentDate)
        }
    }
    val currentDay = dayViewModel.day.collectAsState().value

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        val defaultNutriments = Nutriments(calories = 0.0, carbohydrates = 0.0, protein = 0.0, fats = 0.0)
        //val defaultTarget = Nutriments(calories = 2000.0, carbohydrates = 300.0, protein = 50.0, fats = 70.0)
        val defaultTarget = Nutriments(calories = 1800.0, carbohydrates = 200.0, protein = 20.0, fats = 20.0)
        val defaultDate = LocalDate.parse("1970-01-01")

        AddButtonGroup(
            onAddDishClick = { navController.navigate("dishOverview") },
            onAddFoodClick = { navController.navigate("foodOverview") }
        )

        Log.e("OverviewPage: CurrentDay:", currentDay.toString())
        DayOverview(
            date = currentDay?.date ?: defaultDate,
            eaten = currentDay?.nutriments ?: defaultNutriments,
            target = currentDay?.target ?: defaultTarget,
            navController = navController
        )
    }
}

// Info: KISS-principle