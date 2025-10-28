package com.example.projet1diiage.home.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

// Etape 4 : créer la base de données Room
// on reference toutes nos DAO

@Database(entities = [MealEntity::class, MealPlanEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun mealDao(): MealDao
    abstract fun mealPlanDao(): MealPlanDao
}
