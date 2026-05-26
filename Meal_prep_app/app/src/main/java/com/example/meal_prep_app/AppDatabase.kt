package com.example.meal_prep_app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// main Room database for storing saved meals locally
@Database(entities = [Meal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // gives access to the DAO methods
    abstract fun mealDao(): MealDao

    companion object {
        // keeping only one database instance for the entier app
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "meal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}