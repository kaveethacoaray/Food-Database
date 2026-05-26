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

class SearchMealsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Meal_prep_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SearchMealsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SearchMealsScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // keeping the typed search text and current result state after rotation
    var searchText by rememberSaveable { mutableStateOf("") }
    var statusMessage by rememberSaveable { mutableStateOf("") }
    var savedMealsJson by rememberSaveable { mutableStateOf("") }

    // actual meal objects used to show the result list
    var meals by remember { mutableStateOf(listOf<Meal>()) }

    // rebuilding the meal list from saved JSON when the screen is recreated
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
            text = "Search Meals in Database",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // input box for meal name or ingredient text
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter meal name or ingredient") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // searches the Room database using the DAO query
        Button(
            onClick = {
                if (searchText.isBlank()) {
                    statusMessage = "Please enter a meal name or ingredient"
                    return@Button
                }
                statusMessage = "Searching..."
                scope.launch {
                    // doing the database search in the background
                    val result = withContext(Dispatchers.IO) {
                        val db = AppDatabase.getDatabase(context)
                        db.mealDao().searchMeals(searchText.trim())
                    }
                    meals = result
                    // saving results as JSON so they can be restored after rotation
                    savedMealsJson = MealJsonConverter.serialize(result)
                    statusMessage = if (result.isEmpty()) "No meals found" else ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Search")
        }
        // small message area for empty search, searching state, or no results
        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // showing the matched meals in a scrollable list
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(meals) { meal ->
                MealCardWithImage(meal = meal)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}

