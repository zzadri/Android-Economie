package com.example.projet1diiage.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object AddMeal : Screen("add_meal")
    data object Plan : Screen("plan")
    data object Shopping : Screen("shopping")
    data object Profile : Screen("profile")
    data class MealDetails(val mealId: Long = 0) : Screen("meal_details/$mealId") {
        companion object {
            const val route = "meal_details/{mealId}"
            fun createRoute(mealId: Long) = "meal_details/$mealId"
        }
    }
    data class EditMeal(val mealId: Long = 0) : Screen("edit_meal/$mealId") {
        companion object {
            const val route = "edit_meal/{mealId}"
            fun createRoute(mealId: Long) = "edit_meal/$mealId"
        }
    }
}
