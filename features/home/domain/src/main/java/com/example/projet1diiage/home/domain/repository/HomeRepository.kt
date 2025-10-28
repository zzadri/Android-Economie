package com.example.projet1diiage.home.domain.repository

import com.example.projet1diiage.home.domain.model.Meal
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAllMeals(): Flow<List<Meal>>
    suspend fun getMeal(id: Long): Meal?
    suspend fun insertMeal(meal: Meal): Long
    suspend fun updateMeal(meal: Meal)
    suspend fun deleteMeal(id: Long)
}