// Name = B.K.D.Cooray
// student ID = w2120640/20231311
// vedio demo : https://drive.google.com/file/d/1AgjCDT8PG3ssAcUXPhmYpuRrknkmluJI/view?usp=sharing

package com.example.meal_prep_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.meal_prep_app.ui.theme.Meal_prep_appTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Meal_prep_appTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // using this scope to run database work without blocking the UI
    val scope = rememberCoroutineScope()
    // message shown after inserting the hardcoded meals
    var statusMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Meal Prep App",
            fontSize = 26.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // adding the meals from HardcodedMeals into Room
        Button(
            onClick = {
                scope.launch {
                    val db = AppDatabase.getDatabase(context)
                    val dao = db.mealDao()
                    val meals = HardcodedMeals.getMeals()
                    for (meal in meals) {
                        dao.insertMeal(meal)
                    }
                    statusMessage = "Meals added to database!"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Add Meals to DB")
        }

        // opens the screen that searches meals by ingredient from the API
        Button(
            onClick = {
                val intent = Intent(context, SearchByIngredientActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Search for Meals By Ingredient")
        }

        // opens the local Room search screen
        Button(
            onClick = {
                val intent = Intent(context, SearchMealsActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Search for Meals")
        }

        // opens the web search screen that searches by meal name
        Button(
            onClick = {
                val intent = Intent(context, SearchWebActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Search Meals Online")
        }

        // showing feedback after the DB insert finishes
        if (statusMessage.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = statusMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}