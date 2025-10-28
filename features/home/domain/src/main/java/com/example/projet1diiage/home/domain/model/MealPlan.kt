package com.example.projet1diiage.home.domain.model

import java.time.LocalDate

enum class MealSlot { LUNCH, DINNER }

data class MealPlanEntry(
    val id: Long = 0,
    val date: LocalDate,
    val slot: MealSlot,
    val mealId: Long?
)

