package com.example.meal_prep_app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealDao {

    // adding one meal into Room
    // IGNORE is used so pressing the insert button again does not crash the app
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeal(meal: Meal)

    // simple query for getting everything from the meals table
    @Query("SELECT * FROM meals")
    suspend fun getAllMeals(): List<Meal>

    // searching saved meals by meal name or any ingredient field
    // LOWER + LIKE is used so partial text matches still work even with different letter cases
    @Query("""
        SELECT * FROM meals WHERE
        LOWER(mealName) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient1) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient2) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient3) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient4) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient5) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient6) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient7) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient8) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient9) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient10) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient11) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient12) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient13) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient14) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient15) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient16) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient17) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient18) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient19) LIKE LOWER('%' || :query || '%') OR
        LOWER(ingredient20) LIKE LOWER('%' || :query || '%')
    """)
    suspend fun searchMeals(query: String): List<Meal>
}