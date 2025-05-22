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
    selectedMealsRaw: String,
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

    // Deserialisieren von selectedMeals
    val initialMeals = if (selectedMealsRaw.isNotEmpty()) selectedMealsRaw.split(",") else emptyList()
    var selectedMeals by remember { mutableStateOf(initialMeals.toList()) }
    Log.e("MealAdder Init selectedMeals:", selectedMeals.toString())

    LaunchedEffect(Unit) {
        if (selectedMeals.isEmpty()) {
            selectedMeals = fetchSelectedMeals(mealTime, currentDay)
            Log.e("Reset of obj-Store:", selectedMeals.toString())
        }
    }

    // Observe results from BarcodeScanner via SavedStateHandle
    val currentEntry = navController.currentBackStackEntry
    val savedStateHandle = currentEntry?.savedStateHandle
    LaunchedEffect(Unit) {
        savedStateHandle?.getLiveData<List<String>>("selectedMeals")
            ?.observe(currentEntry) { result ->
                Log.e("MealAdder result: ", result.toString())
                // Only update if result is not null
                if (result != null) {
                    selectedMeals = result
                }
            }
    }

    val mealsToAdd = mutableListOf<Meal>()
    var totalCalories = remember { mutableStateOf(0.0) }
    var totalProtein = remember { mutableStateOf(0.0) }
    var totalCarbohydrates = remember { mutableStateOf(0.0) }
    var totalFats = remember { mutableStateOf(0.0) }

    LaunchedEffect(selectedMeals) {
        totalCalories.value = 0.0
        totalProtein.value = 0.0
        totalCarbohydrates.value = 0.0
        totalFats.value = 0.0
        mealsToAdd.clear()

        for (mealName in selectedMeals) {
            // Get Food or Dish
            val food = foods.find { it.name == mealName }
            val dish = dishes.find { it.name == mealName }

            if (food != null) {
                totalCalories.value += food.nutriments.calories
                totalProtein.value += food.nutriments.protein
                totalCarbohydrates.value += food.nutriments.carbohydrates
                totalFats.value += food.nutriments.fats

                mealsToAdd.add(
                    Meal(
                        id = (0..1000).random(),
                        food = food,
                        dish = null
                    )
                )
            } else if (dish != null) {
                totalCalories.value += dish.nutriments.calories
                totalProtein.value += dish.nutriments.protein
                totalCarbohydrates.value += dish.nutriments.carbohydrates
                totalFats.value += dish.nutriments.fats

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
                    navController.navigate("barcodeScanner?selectedMeals=${selectedMeals.joinToString(",")}")
                }) {
                    val isDarkMode = isSystemInDarkTheme()
                    Image(
                        painter = painterResource(
                            id = if (isDarkMode) R.drawable.barcode_darkmode else R.drawable.barcode
                        ),
                        contentDescription = "Barcode Icon",
                        modifier = Modifier.size(48.dp).padding(4.dp),
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

            val caloriesProgress = totalCalories.value.toFloat() / target.calories.toFloat()
            NutrientStatusBar("Calories: ${totalCalories.value} / ${target.calories}", caloriesProgress)

            val carbohydratesProgress = totalCarbohydrates.value.toFloat() / target.carbohydrates.toFloat()
            NutrientStatusBar("Carbohydrates: ${totalCarbohydrates.value} / ${target.carbohydrates}", carbohydratesProgress)

            val proteinProgress = totalProtein.value.toFloat() / target.protein.toFloat()
            NutrientStatusBar("Protein: ${totalProtein.value} / ${target.protein}", proteinProgress)

            val fatProgress = totalFats.value.toFloat() / target.fats.toFloat()
            NutrientStatusBar("Fats: ${totalFats.value} / ${target.fats}", fatProgress)

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
                    Log.e("Not Update CurrentDay: ", currentDay.toString())

                    // Implement function that list of the meals that are in currentDay.breakfast but not in mealsToAdd
                    var removedMeals: List<Meal> = emptyList()
                    if (mealTime == "Breakfast") {
                        removedMeals = getRemovedMeals(currentDay.breakfast, mealsToAdd)
                        currentDay.breakfast = mealsToAdd // TODO: Check "=" statt "+=" sollte abgewählte Meals entfernen
                    } else if (mealTime == "Lunch") {
                        removedMeals = getRemovedMeals(currentDay.lunch, mealsToAdd)
                        currentDay.lunch = mealsToAdd
                    } else if (mealTime == "Dinner") {
                        removedMeals = getRemovedMeals(currentDay.dinner, mealsToAdd)
                        currentDay.dinner = mealsToAdd
                    } else if (mealTime == "Snack") {
                        removedMeals = getRemovedMeals(currentDay.snack, mealsToAdd)
                        currentDay.snack = mealsToAdd
                    } else {
                        Log.e("MealAdder_Finish-Button: If-Case:", mealTime)
                    }

                    subtractRemovedMealNutrients(
                        removedMeals,
                        totalCalories,
                        totalProtein,
                        totalCarbohydrates,
                        totalFats
                    )

                    currentDay.nutriments.calories += totalCalories.value
                    currentDay.nutriments.protein += totalProtein.value
                    currentDay.nutriments.carbohydrates += totalCarbohydrates.value
                    currentDay.nutriments.fats += totalFats.value

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
    } else if (mealTime == "Lunch") {
        mealsObj = currentDay.lunch
    } else if (mealTime == "Dinner") {
        mealsObj = currentDay.dinner
    } else if (mealTime == "Snack") {
        mealsObj = currentDay.snack
    } else {
        Log.e("MealAdder getMealTimeObjs: ", mealTime)
    }

    return mealsObj
}

private fun getRemovedMeals(
    mealTimeList: List<Meal>,
    mealsToAdd: List<Meal>
): List<Meal> {
    return mealTimeList.filter { mealTime ->
        mealsToAdd.none { it.id == mealTime.id }
    }
}

private fun subtractRemovedMealNutrients(
    removedMeals: List<Meal>,
    totalCalories: MutableState<Double>,
    totalProtein: MutableState<Double>,
    totalCarbohydrates: MutableState<Double>,
    totalFats: MutableState<Double>
) {
    var removedCalories = 0.0
    var removedProtein = 0.0
    var removedCarbohydrates = 0.0
    var removedFats = 0.0

    for (meal in removedMeals) {
        meal.food?.let {
            removedCalories += it.nutriments.calories
            removedProtein += it.nutriments.protein
            removedCarbohydrates += it.nutriments.carbohydrates
            removedFats += it.nutriments.fats
        }
        meal.dish?.let {
            removedCalories += it.nutriments.calories
            removedProtein += it.nutriments.protein
            removedCarbohydrates += it.nutriments.carbohydrates
            removedFats += it.nutriments.fats
        }
    }

    totalCalories.value -= removedCalories
    totalProtein.value -= removedProtein
    totalCarbohydrates.value -= removedCarbohydrates
    totalFats.value -= removedFats
}