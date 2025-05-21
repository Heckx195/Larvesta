package de.larvesta.presentation.ui.components.meal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import de.larvesta.R
import de.larvesta.domain.model.Dish
import de.larvesta.domain.model.Food
import de.larvesta.domain.model.Meal
import de.larvesta.domain.model.Nutriments
import de.larvesta.presentation.ui.components.common.GoBackButton
import de.larvesta.presentation.ui.components.day.NutrientStatusBar
import de.larvesta.presentation.viewmodel.DayViewModel
import de.larvesta.presentation.viewmodel.DishViewModel
import de.larvesta.presentation.viewmodel.FoodViewModel
import android.util.Log
import java.time.LocalDate
import de.larvesta.domain.model.Day
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealAdder(
    mealTime: String,
    foodViewModel: FoodViewModel,
    dishViewModel: DishViewModel,
    dayViewModel: DayViewModel,
    navController: NavHostController
) {
    val foods: List<Food> = foodViewModel.foods.collectAsState().value
    val dishes: List<Dish> = dishViewModel.dishes.collectAsState().value

    val currentDate = LocalDate.now()
    dayViewModel.fetchDayByDate(currentDate)
    var currentDay = dayViewModel.day.collectAsState().value
    if (currentDay == null) {
        currentDay = Day(
            id = 0, // auto-generated
            date = LocalDate.now().minusDays(1),
            nutriments = Nutriments(0.0, 0.0, 0.0, 0.0),
            target = Nutriments(1000.0, 130.0, 15.0, 17.0),
            breakfast = listOf(Meal(id=994, food=Food(id=367, name="Banana", barcode="", nutriments=Nutriments(calories=50.0, protein=2.0, fats=2.0, carbohydrates=2.0), lastUsed= Date(1), category="Fruit"), dish=null), Meal(id=474, food=Food(id=388, name="Apple", barcode="", nutriments=Nutriments(calories=50.0, protein=2.0, fats=2.0, carbohydrates=2.0), lastUsed=Date(1), category="Fruit"), dish=null), Meal(id=986, food=Food(id=410, name="Lemon", barcode="", nutriments=Nutriments(calories=20.0, protein=2.0, fats=1.0, carbohydrates=1.0), lastUsed=Date(1), category="Fruit"), dish=null)),
            lunch = emptyList(),
            dinner = emptyList(),
            snack = emptyList()
        )
    }

    var meals: List<String> = emptyList()
    for (food in foods) {
        meals = meals.plus(food.name)
    }
    for (dish in dishes) {
        meals = meals.plus(dish.name)
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = meals.filter { it.contains(searchQuery, ignoreCase = true) }

    var selectedMeals by remember { mutableStateOf(emptyList<String>()) }
    if (selectedMeals.isEmpty()) {
        selectedMeals = fetchSelectedMeals(mealTime, currentDay)
        Log.e("MealAdder fetchMeals:", selectedMeals.toString())
    }

    LaunchedEffect(navController.currentBackStackEntry?.arguments?.getStringArrayList("selectedMeals")) {
        val updatedMeals = navController.currentBackStackEntry
            ?.arguments
            ?.getStringArrayList("selectedMeals")
        if (updatedMeals != null) {
            selectedMeals = updatedMeals
        }
    }

    val mealsToAdd = mutableListOf<Meal>()

    var totalCalories by remember { mutableStateOf(0.0) }
    var totalProtein by remember { mutableStateOf(0.0) }
    var totalCarbohydrates by remember { mutableStateOf(0.0) }
    var totalFats by remember { mutableStateOf(0.0) }

    LaunchedEffect(selectedMeals) {
        totalCalories = 0.0
        totalProtein = 0.0
        totalCarbohydrates = 0.0
        totalFats = 0.0
        mealsToAdd.clear()

        for (mealName in selectedMeals) {
            // Get Food or Dish
            val food = foods.find { it.name == mealName }
            val dish = dishes.find { it.name == mealName }

            if (food != null) {
                totalCalories += food.nutriments.calories
                totalProtein += food.nutriments.protein
                totalCarbohydrates += food.nutriments.carbohydrates
                totalFats += food.nutriments.fats

                mealsToAdd.add(
                    Meal(
                        id = (0..1000).random(),
                        food = food,
                        dish = null
                    )
                )
            } else if (dish != null) {
                totalCalories += dish.nutriments.calories
                totalProtein += dish.nutriments.protein
                totalCarbohydrates += dish.nutriments.carbohydrates
                totalFats += dish.nutriments.fats

                mealsToAdd.add(
                    Meal(
                        id = (0..1000).random(),
                        food = null,
                        dish = dish
                    )
                )
            }
        }
    }

    LazyColumn(
    ) {
        // TopBar
        item {
            TopAppBar(
                title = { Text(text = mealTime, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { GoBackButton(onClick = { navController.popBackStack() } ) }
            )
        }

        // SearchBar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                        .height(48.dp)
                        .background(Color.LightGray, MaterialTheme.shapes.small)
                        .padding(horizontal = 8.dp),
                    textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                    decorationBox = { innerTextField ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                tint = Color.Gray,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Box(modifier = Modifier.weight(1f)) {
                                innerTextField()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = {
                    navController.currentBackStackEntry?.arguments?.putStringArrayList(
                        "selectedMeals",
                        ArrayList(selectedMeals)
                    )
                    navController.navigate("barcodeScanner")
                }) {
                    val isDarkMode = isSystemInDarkTheme()
                    Image(
                        painter = painterResource(
                            id = if (isDarkMode) R.drawable.barcode_darkmode else R.drawable.barcode
                        ),
                        contentDescription = "Barcode Icon",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(4.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        // Nutriments bars
        item {
            // TODO: totalValues + eaten Nutriments if real total of day
            // val defaultNutriments = Nutriments(calories = 0.0, carbohydrates = 0.0, protein = 0.0, fats = 0.0)
            // val eaten = currentDay?.nutriments ?: defaultNutriments

            val defaultTarget = Nutriments(calories = 2000.0, carbohydrates = 300.0, protein = 50.0, fats = 70.0)
            val target = currentDay.target ?: defaultTarget

            val caloriesProgress = totalCalories.toFloat() / target.calories.toFloat()
            NutrientStatusBar("Calories: $totalCalories / ${target.calories}", caloriesProgress)

            val carbohydratesProgress = totalCarbohydrates.toFloat() / target.carbohydrates.toFloat()
            NutrientStatusBar("Carbohydrates: $totalCarbohydrates / ${target.carbohydrates}", carbohydratesProgress)

            val proteinProgress = totalProtein.toFloat() / target.protein.toFloat()
            NutrientStatusBar("Protein: $totalProtein / ${target.protein}", proteinProgress)

            val fatProgress = totalFats.toFloat() / target.fats.toFloat()
            NutrientStatusBar("Fats: $totalFats / ${target.fats}", fatProgress)

            Spacer(modifier = Modifier.height(32.dp))
        }

        // List of filtered meals.
        items(filteredItems) { meal ->
            val isSelected = selectedMeals.contains(meal)
            val (mealObj, type) = getMealByName(meal, foods, dishes)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        selectedMeals = if (isSelected) {
                            selectedMeals - meal
                        } else {
                            selectedMeals + meal
                        }
                    },
                border = if (isSelected) {
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                } else {
                    null
                }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    if (mealObj != null) {
                        Text(
                            text = mealObj.food?.name ?: mealObj.dish?.name ?: "Unknown",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = if (type == 0) "Food" else "Dish",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    if (mealTime == "Breakfast") {
                        currentDay.breakfast = mealsToAdd // TODO: Check "=" statt "+=" sollte abgewählte Meals entfernen
                    } else if (mealTime == "Lunch") {
                        currentDay.lunch = mealsToAdd
                    } else if (mealTime == "Dinner") {
                        currentDay.dinner = mealsToAdd
                    } else if (mealTime == "Snack") {
                        currentDay.snack = mealsToAdd
                    } else {
                        Log.e("MealAdder_Finish-Button:", "error in mealTime if-case")
                    }

                    currentDay.nutriments.calories += totalCalories
                    currentDay.nutriments.protein += totalProtein
                    currentDay.nutriments.carbohydrates += totalCarbohydrates
                    currentDay.nutriments.fats += totalFats

                    Log.e("UpdateCurrentDay: ", currentDay.toString())

                    dayViewModel.updateDay(currentDay)

                    navController.popBackStack()
                }
            ) {
                Text("Finish")
            }
        }
    }
}

// Private helper functions.
private fun getMealByName(
    name: String,
    foods: List<Food>,
    dishes: List<Dish>
): Pair<Meal?, Int?> {
    val food = foods.find { it.name == name }
    if (food != null) {
        return Pair(
            Meal(
                id = (0..1000).random(),
                food = food,
                dish = null
            ),
            0
        )
    }

    val dish = dishes.find { it.name == name }
    if (dish != null) {
        return Pair(
            Meal(
                id = (0..1000).random(),
                food = null,
                dish = dish
            ),
            1
        )
    }

    return Pair(null, null)
}

private fun getNameByMeal(
    meal: Meal
) : String {
    if (meal.food != null) {
        return meal.food!!.name
    } else if (meal.dish != null) {
        return meal.dish!!.name
    } else {
        return "ERROR: at meal: $meal"
    }
}

private fun fetchSelectedMeals(
    mealTime: String,
    currentDay: Day
) : List<String> {
    Log.e("fetchSelectedMeals:", "Reset auf obj stored meals")

    val mealsObj: List<Meal> = getMealTimeObjs(mealTime, currentDay)
    val mealsString = emptyList<String>().toMutableList()

    for (meal in mealsObj) {
        mealsString += getNameByMeal(meal)
    }

    return mealsString
}

private fun getMealTimeObjs(
    mealTime: String,
    currentDay: Day,
) : List<Meal> {
    var mealsObj: List<Meal> = emptyList()

    if (mealTime == "Breakfast") {
        mealsObj = currentDay.breakfast
    } else if (mealTime == "lunch") {
        mealsObj = currentDay.lunch
    } else if (mealTime == "dinner") {
        mealsObj = currentDay.dinner
    } else if (mealTime == "snack") {
        mealsObj = currentDay.snack
    } else {
        Log.e("--- GetMealTime: ", "ERROR in if-case")
    }

    return mealsObj
}