package com.example.meal_prep_app

import org.json.JSONArray
import org.json.JSONObject

// turns meal lists into a json string and back again
// used so searched meals can be kept in rememberSaveable after rotation
object MealJsonConverter {

    // converting each meal into JSON before saving it as a string
    fun serialize(meals: List<Meal>): String {
        val array = JSONArray()
        for (meal in meals) {
            val obj = JSONObject()
            obj.put("mealName", meal.mealName)
            obj.put("drinkAlternate", meal.drinkAlternate)
            obj.put("category", meal.category)
            obj.put("area", meal.area)
            obj.put("instructions", meal.instructions)
            obj.put("mealThumb", meal.mealThumb)
            obj.put("tags", meal.tags)
            obj.put("youtube", meal.youtube)
            // storing all ingredient and measure fields too
            for (i in 1..20) {
                obj.put("ingredient$i", getMealIngredient(meal, i))
                obj.put("measure$i", getMealMeasure(meal, i))
            }
            array.put(obj)
        }
        return array.toString()
    }

    // rebuilding Meal objects from the saved json string
    fun deserialize(json: String): List<Meal> {
        val meals = mutableListOf<Meal>()
        // nothing to rebuild if the saved string is empty
        if (json.isEmpty()) return meals
        val array = JSONArray(json)
        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            // using optString so missing values just become empty strings
            fun str(key: String) = obj.optString(key, "")
            meals.add(
                Meal(
                    mealName = str("mealName"),
                    drinkAlternate = str("drinkAlternate"),
                    category = str("category"),
                    area = str("area"),
                    instructions = str("instructions"),
                    mealThumb = str("mealThumb"),
                    tags = str("tags"),
                    youtube = str("youtube"),
                    ingredient1 = str("ingredient1"), ingredient2 = str("ingredient2"),
                    ingredient3 = str("ingredient3"), ingredient4 = str("ingredient4"),
                    ingredient5 = str("ingredient5"), ingredient6 = str("ingredient6"),
                    ingredient7 = str("ingredient7"), ingredient8 = str("ingredient8"),
                    ingredient9 = str("ingredient9"), ingredient10 = str("ingredient10"),
                    ingredient11 = str("ingredient11"), ingredient12 = str("ingredient12"),
                    ingredient13 = str("ingredient13"), ingredient14 = str("ingredient14"),
                    ingredient15 = str("ingredient15"), ingredient16 = str("ingredient16"),
                    ingredient17 = str("ingredient17"), ingredient18 = str("ingredient18"),
                    ingredient19 = str("ingredient19"), ingredient20 = str("ingredient20"),
                    measure1 = str("measure1"), measure2 = str("measure2"),
                    measure3 = str("measure3"), measure4 = str("measure4"),
                    measure5 = str("measure5"), measure6 = str("measure6"),
                    measure7 = str("measure7"), measure8 = str("measure8"),
                    measure9 = str("measure9"), measure10 = str("measure10"),
                    measure11 = str("measure11"), measure12 = str("measure12"),
                    measure13 = str("measure13"), measure14 = str("measure14"),
                    measure15 = str("measure15"), measure16 = str("measure16"),
                    measure17 = str("measure17"), measure18 = str("measure18"),
                    measure19 = str("measure19"), measure20 = str("measure20")
                )
            )
        }
        return meals
    }
}