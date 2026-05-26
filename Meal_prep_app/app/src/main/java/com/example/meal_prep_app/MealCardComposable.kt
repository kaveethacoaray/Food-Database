package com.example.meal_prep_app

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// reusable card for showing full meal details on different screens
@Composable
fun MealCardWithImage(meal: Meal) {
    // keeping the downloaded image in compose state
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    // loading the thumbnail when this meal card appears or when the image url changes
    LaunchedEffect(meal.mealThumb) {
        if (meal.mealThumb.isNotBlank()) {
            // doing image loading in IO since this is network work
            withContext(Dispatchers.IO) {
                bitmap = ImageLoader.loadBitmap(meal.mealThumb)
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // only showing the image if it was loaded properly
            bitmap?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = meal.mealName,
                        modifier = Modifier.size(160.dp)
                    )
                }
            }

            // meal title at the top of the card
            Text(
                text = meal.mealName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Basic details section
            MealDetailRow(label = "Category", value = meal.category)
            MealDetailRow(label = "Area", value = meal.area)
            MealDetailRow(label = "Tags", value = meal.tags)
            MealDetailRow(label = "Drink Alternate", value = meal.drinkAlternate)
            MealDetailRow(label = "Youtube", value = meal.youtube)

            Spacer(modifier = Modifier.height(8.dp))

            // showing the full instructions text under its own heading
            Text(
                text = "Instructions",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = meal.instructions,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // looping through the 20 ingredient and measure fields from the meal object
            Text(
                text = "Ingredients",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            for (i in 1..20) {
                val ingredient = getMealIngredient(meal, i)
                val measure = getMealMeasure(meal, i)
                // skipping empty ingredient slots so only real values are shown
                if (ingredient.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = ingredient,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = measure,
                            fontSize = 13.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

// small row used for category, area, tags and similar fields
@Composable
fun MealDetailRow(label: String, value: String) {
    // hiding rows when the value is empty
    if (value.isNotBlank()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            Text(
                text = "$label: ",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Text(
                text = value,
                fontSize = 13.sp
            )
        }
    }
}