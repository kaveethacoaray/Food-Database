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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meal_prep_app.ui.theme.Meal_prep_appTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchWebActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Meal_prep_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SearchWebScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SearchWebScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    // keeping the typed text and current screen state after rotation
    var searchText by rememberSaveable { mutableStateOf("") }
    var statusMessage by rememberSaveable { mutableStateOf("") }
    var savedMealsJson by rememberSaveable { mutableStateOf("") }
    // actual meal objects used to show the results
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
            text = "Search Meals Online",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // input box for searching meals by name from the API
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter meal name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // searches the web service using the typed meal name
        Button(
            onClick = {
                if (searchText.isBlank()) {
                    statusMessage = "Please enter a meal name"
                    return@Button
                }
                statusMessage = "Searching..."
                scope.launch {
                    // doing the API call in the background
                    val result = withContext(Dispatchers.IO) {
                        NetworkHelper.getMealsByName(searchText.trim())
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

        // feedback for empty input, loading, or no results
        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // showing the found meals in the same shared card layout
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(meals) { meal ->
                MealCardWithImage(meal = meal)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}