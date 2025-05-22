package de.larvesta.presentation.ui.components.overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import de.larvesta.presentation.ui.components.day.DayOverview
import de.larvesta.presentation.viewmodel.DayViewModel

@Composable
fun OverviewPage(
    navController: NavHostController,
    dayViewModel: DayViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        AddButtonGroup(
            onAddDishClick = { navController.navigate("dishOverview") },
            onAddFoodClick = { navController.navigate("foodOverview") }
        )

        DayOverview(
            dayViewModel = dayViewModel,
            navController = navController
        )
    }
}

// Info: KISS-principle