package com.example.projet1diiage.home.domain.repository

import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.model.MealPlanEntry
import com.example.projet1diiage.home.domain.model.MealSlot
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PlanningRepository {
    suspend fun generateMonthlyPlan(
        month: Int,
        year: Int,
        includeLunch: Boolean,
        includeDinner: Boolean,
        includeWeekend: Boolean
    )

    fun getWeekPlan(weekStart: LocalDate): Flow<List<MealPlanEntry>>

    suspend fun assignMeal(date: LocalDate, slot: MealSlot, mealId: Long)

    suspend fun clearAssignment(date: LocalDate, slot: MealSlot)

    suspend fun getMealsForEntries(entries: List<MealPlanEntry>): List<Meal>

    suspend fun getShoppingListForWeek(weekStart: LocalDate): List<String>

    suspend fun randomizeWeek(weekStart: LocalDate, includeWeekend: Boolean)
}
