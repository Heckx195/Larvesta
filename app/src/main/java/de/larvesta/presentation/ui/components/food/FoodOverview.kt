package de.larvesta.presentation.ui.components.food

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.larvesta.R
import de.larvesta.presentation.ui.components.common.GoBackButton
import de.larvesta.presentation.viewmodel.FoodViewModel

@Composable
fun FoodOverview(
    navController: NavHostController,
    foodViewModel: FoodViewModel,
    modifier: Modifier = Modifier,
) {
    val foods = foodViewModel.foods.collectAsState().value.sortedBy { it.name }

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
                text = "My Foods:",
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
                    items(foods) { food ->
                        FoodEntry(food) {
                            navController.navigate("foodDetail/${food.id}")
                        }
                    }
                }
            }
            Row {
                AddFoodButton(foodViewModel)

                IconButton(
                    onClick = {
                        navController.navigate("barcodeScanner")
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    val isDarkMode = isSystemInDarkTheme()
                    Image(
                        painter = painterResource(
                            id = if (isDarkMode) R.drawable.barcode_darkmode else R.drawable.barcode
                        ),
                        contentDescription = "Barcode Icon",
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }
    }
}

// Info: KISS-principle