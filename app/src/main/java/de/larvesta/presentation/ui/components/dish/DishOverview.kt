package de.larvesta.presentation.ui.components.dish

import de.larvesta.presentation.viewmodel.DishViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.larvesta.presentation.ui.components.common.GoBackButton
import de.larvesta.presentation.viewmodel.FoodViewModel

@Composable
fun DishOverview(
    navController: NavHostController,
    dishViewModel: DishViewModel,
    foodViewModel: FoodViewModel,
    modifier: Modifier = Modifier,
) {
    val dishes = dishViewModel.dishes.collectAsState().value.sortedBy { it.name }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .height(56.dp)
            ) {
                GoBackButton(
                    onClick = { navController.popBackStack() }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
        ) {
            Text(
                text = "My Dishes:",
                fontSize = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = TextStyle(
                    textAlign = TextAlign.Center
                )
            )
            Box(Modifier.height(300.dp)) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 128.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(dishes) { dish ->
                        DishEntry(dish) {
                            navController.navigate("dishDetail/${dish.id}")
                        }
                    }
                }
            }
            AddDishButton(
                dishViewModel,
                foodViewModel
            )
        }
    }
}

// Info: KISS-principle