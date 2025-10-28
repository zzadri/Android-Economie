package com.example.projet1diiage.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Etape 2 : créer l'entité Room en gros les table de la BDD
@Entity(tableName = "meal_plan")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // yyyy-MM-dd
    val slot: String, // LUNCH or DINNER
    val mealId: Long?
)

