package de.larvesta.presentation.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.larvesta.presentation.ui.components.barcode.BarcodeScanner
import de.larvesta.presentation.ui.components.meal.MealAdder
import de.larvesta.presentation.ui.components.dish.DishOverview
import de.larvesta.presentation.ui.components.food.DishDetail
import de.larvesta.presentation.ui.components.food.FoodDetail
import de.larvesta.presentation.ui.components.food.FoodOverview
import de.larvesta.presentation.ui.components.overview.OverviewPage
import de.larvesta.presentation.viewmodel.DayViewModel
import de.larvesta.presentation.viewmodel.DishViewModel
import de.larvesta.presentation.viewmodel.FoodViewModel
import java.util.concurrent.ExecutorService

@Composable
fun MyApp(
    cameraExecutor: ExecutorService,
    dishViewModel: DishViewModel,
    foodViewModel: FoodViewModel,
    dayViewModel: DayViewModel
) {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        NavHost(
            navController, startDestination = "overviewPage",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            composable("overviewPage") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    OverviewPage(
                        navController,
                        dayViewModel
                    )
                }
            }
            composable(
                "dishOverview",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    DishOverview(
                        navController,
                        dishViewModel,
                        foodViewModel
                    )
                }
            }
            composable(
                "foodOverview",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    FoodOverview(
                        navController,
                        foodViewModel
                    )
                }
            }
            composable(
                "barcodeScanner?selectedMeals={selectedMeals}",
                arguments = listOf(navArgument("selectedMeals") { nullable = true }),
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(300, easing = LinearEasing)
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(300, easing = LinearEasing)
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) { backStackEntry ->
                Column(modifier = Modifier.fillMaxSize()) {
                    BarcodeScanner(
                        navController,
                        cameraExecutor,
                        foodViewModel
                    )
                }
            }

            composable(
                "mealAdder/{mealType}/{currentDate}?selectedMeals={selectedMeals}",
                arguments = listOf(
                    navArgument("selectedMeals") { nullable = true }
                ),
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(300, easing = LinearEasing)
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(300, easing = LinearEasing)
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) { backStackEntry ->
                val mealType = backStackEntry.arguments?.getString("mealType") ?: "Default"
                val currentDate = backStackEntry.arguments?.getString("currentDate") ?: "1970-01-01"
                val selectedMeals = backStackEntry.arguments?.getString("selectedMeals") ?: ""
                MealAdder(
                    mealType,
                    selectedMeals,
                    currentDate,
                    foodViewModel,
                    dishViewModel,
                    dayViewModel,
                    navController
                )
            }
            composable(
                "foodDetail/{foodId}",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) { backStackEntry ->
                val dayId = backStackEntry.arguments?.getString("foodId")?.toInt()
                val foods = foodViewModel.foods.collectAsState().value
                val day = foods.find { it.id == dayId }
                day?.let { FoodDetail(it, navController) }
            }
            composable(
                "dishDetail/{dishId}",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = AnimatedContentTransitionScope.SlideDirection.Start
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(
                            300, easing = LinearEasing
                        )
                    ) + slideOutOfContainer(
                        animationSpec = tween(300, easing = EaseOut),
                        towards = AnimatedContentTransitionScope.SlideDirection.End
                    )
                }
            ) { backStackEntry ->
                val dayId = backStackEntry.arguments?.getString("dishId")?.toInt()
                val dish = dishViewModel.dishes.collectAsState().value
                val day = dish.find { it.id == dayId }
                day?.let { DishDetail(it, navController) }
            }
        }
    }
}