package com.example.meal_prep_app

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// handles the API calls and turns the JSON into Meal objects
object NetworkHelper {

    // first gets the matching meal ids from the ingredient search
    // then does another call for each id to get the full meal details
    fun getMealsByIngredient(ingredient: String): List<Meal> {
        val urlString = "https://www.themealdb.com/api/json/v1/1/filter.php?i=${ingredient.replace(" ", "_")}"
        val idList = fetchMealIds(urlString)
        val meals = mutableListOf<Meal>()
        for (id in idList) {
            val meal = getMealById(id)
            if (meal != null) meals.add(meal)
        }
        return meals
    }

    // searches meals online by name and returns the full meal list directly
    fun getMealsByName(name: String): List<Meal> {
        val urlString = "https://www.themealdb.com/api/json/v1/1/search.php?s=${name.replace(" ", "%20")}"
        val jsonString = fetchRawJson(urlString) ?: return emptyList()
        return parseMealsFromJson(jsonString)
    }

    // ingredient search API only gives short results, so this part collects just the ids first
    private fun fetchMealIds(urlString: String): List<String> {
        val jsonString = fetchRawJson(urlString) ?: return emptyList()
        val ids = mutableListOf<String>()
        try {
            val root = JSONObject(jsonString)
            val mealsArray: JSONArray = root.optJSONArray("meals") ?: return emptyList()
            for (i in 0 until mealsArray.length()) {
                val meal = mealsArray.getJSONObject(i)
                ids.add(meal.getString("idMeal"))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return ids
    }

    // second API call for ingredient search to get one full meal using its id
    private fun getMealById(id: String): Meal? {
        val urlString = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=$id"
        val jsonString = fetchRawJson(urlString) ?: return null
        val meals = parseMealsFromJson(jsonString)
        return if (meals.isNotEmpty()) meals[0] else null
    }

    // turns the meals array from JSON into a list of Meal objects
    private fun parseMealsFromJson(jsonString: String): List<Meal> {
        val meals = mutableListOf<Meal>()
        try {
            val root = JSONObject(jsonString)
            val mealsArray: JSONArray = root.optJSONArray("meals") ?: return emptyList()
            for (i in 0 until mealsArray.length()) {
                val obj = mealsArray.getJSONObject(i)
                meals.add(parseSingleMeal(obj))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return meals
    }

    // maps one API meal object into the local Meal data class format
    private fun parseSingleMeal(obj: JSONObject): Meal {
        // using empty string if a value is missing or null in the API response
        fun str(key: String): String = obj.optString(key, "")

        return Meal(
            mealName = str("strMeal"),
            drinkAlternate = str("strDrinkAlternate"),
            category = str("strCategory"),
            area = str("strArea"),
            instructions = str("strInstructions"),
            mealThumb = str("strMealThumb"),
            tags = str("strTags"),
            youtube = str("strYoutube"),
            ingredient1 = str("strIngredient1"), ingredient2 = str("strIngredient2"),
            ingredient3 = str("strIngredient3"), ingredient4 = str("strIngredient4"),
            ingredient5 = str("strIngredient5"), ingredient6 = str("strIngredient6"),
            ingredient7 = str("strIngredient7"), ingredient8 = str("strIngredient8"),
            ingredient9 = str("strIngredient9"), ingredient10 = str("strIngredient10"),
            ingredient11 = str("strIngredient11"), ingredient12 = str("strIngredient12"),
            ingredient13 = str("strIngredient13"), ingredient14 = str("strIngredient14"),
            ingredient15 = str("strIngredient15"), ingredient16 = str("strIngredient16"),
            ingredient17 = str("strIngredient17"), ingredient18 = str("strIngredient18"),
            ingredient19 = str("strIngredient19"), ingredient20 = str("strIngredient20"),
            measure1 = str("strMeasure1"), measure2 = str("strMeasure2"),
            measure3 = str("strMeasure3"), measure4 = str("strMeasure4"),
            measure5 = str("strMeasure5"), measure6 = str("strMeasure6"),
            measure7 = str("strMeasure7"), measure8 = str("strMeasure8"),
            measure9 = str("strMeasure9"), measure10 = str("strMeasure10"),
            measure11 = str("strMeasure11"), measure12 = str("strMeasure12"),
            measure13 = str("strMeasure13"), measure14 = str("strMeasure14"),
            measure15 = str("strMeasure15"), measure16 = str("strMeasure16"),
            measure17 = str("strMeasure17"), measure18 = str("strMeasure18"),
            measure19 = str("strMeasure19"), measure20 = str("strMeasure20")
        )
    }

    // basic GET request helper used by both search flows
    private fun fetchRawJson(urlString: String): String? {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connect()
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val result = reader.readText()
            reader.close()
            connection.disconnect()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}