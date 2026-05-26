package com.example.meal_prep_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meal_prep_app.ui.theme.Meal_prep_appTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchByIngredientActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Meal_prep_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SearchByIngredientScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SearchByIngredientScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // keeping the search text and messages after rotation
    var ingredientText by rememberSaveable { mutableStateOf("") }
    var statusMessage by rememberSaveable { mutableStateOf("") }

    // actual meal objects used by the UI
    var meals by remember { mutableStateOf(listOf<Meal>()) }

    // saving the meals as JSON because rememberSaveable cannot directly keep this custom list
    var savedMealsJson by rememberSaveable { mutableStateOf("") }

    // rebuilding the meal list from the saved JSON after rotation
    LaunchedEffect(savedMealsJson) {
        if (savedMealsJson.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                meals = MealJsonConverter.deserialize(savedMealsJson)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Search by Ingredient",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // input box for the ingredient search
        OutlinedTextField(
            value = ingredientText,
            onValueChange = { ingredientText = it },
            label = { Text("Enter ingredient") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            // gets meals from the API using the typed ingredient
            Button(
                onClick = {
                    if (ingredientText.isBlank()) {
                        statusMessage = "Please enter an ingredient"
                        return@Button
                    }
                    statusMessage = "Retrieving meals..."
                    scope.launch {
                        // keeping the network request off the main thread
                        val result = withContext(Dispatchers.IO) {
                            NetworkHelper.getMealsByIngredient(ingredientText.trim())
                        }
                        meals = result
                        // saving the current results so they can be restored after rotation
                        savedMealsJson = MealJsonConverter.serialize(result)
                        statusMessage = if (result.isEmpty()) "No meals found" else ""
                    }
                },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            ) {
                Text("Retrieve Meals")
            }

            // saves the currently retrieved meals into Room
            Button(
                onClick = {
                    if (meals.isEmpty()) {
                        statusMessage = "No meals to save"
                        return@Button
                    }
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            val db = AppDatabase.getDatabase(context)
                            val dao = db.mealDao()
                            for (meal in meals) {
                                dao.insertMeal(meal)
                            }
                        }
                        statusMessage = "Meals saved to database!"
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save meals to Database")
            }
        }

        // small feedback text for empty input, loading result, save result, etc
        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // showing all found meals in a scrollable list
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(meals) { meal ->
                MealCardWithImage(meal = meal)
            }
        }
    }
}

// helper for reading one ingredient field by number
// needed because the meal data stores ingredients in separate numbered fields
fun getMealIngredient(meal: Meal, index: Int): String = when (index) {
    1 -> meal.ingredient1; 2 -> meal.ingredient2; 3 -> meal.ingredient3
    4 -> meal.ingredient4; 5 -> meal.ingredient5; 6 -> meal.ingredient6
    7 -> meal.ingredient7; 8 -> meal.ingredient8; 9 -> meal.ingredient9
    10 -> meal.ingredient10; 11 -> meal.ingredient11; 12 -> meal.ingredient12
    13 -> meal.ingredient13; 14 -> meal.ingredient14; 15 -> meal.ingredient15
    16 -> meal.ingredient16; 17 -> meal.ingredient17; 18 -> meal.ingredient18
    19 -> meal.ingredient19; 20 -> meal.ingredient20; else -> ""
}

// same idea as above but for the measure fields
fun getMealMeasure(meal: Meal, index: Int): String = when (index) {
    1 -> meal.measure1; 2 -> meal.measure2; 3 -> meal.measure3
    4 -> meal.measure4; 5 -> meal.measure5; 6 -> meal.measure6
    7 -> meal.measure7; 8 -> meal.measure8; 9 -> meal.measure9
    10 -> meal.measure10; 11 -> meal.measure11; 12 -> meal.measure12
    13 -> meal.measure13; 14 -> meal.measure14; 15 -> meal.measure15
    16 -> meal.measure16; 17 -> meal.measure17; 18 -> meal.measure18
    19 -> meal.measure19; 20 -> meal.measure20; else -> ""
}