package com.example.projet1diiage.NavHost

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.projet1diiage.home.ui.*
import com.example.projet1diiage.navigation.Screen
import com.example.projet1diiage.profile.ui.ProfileScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isHome = currentDestination.isInHierarchy(Screen.Home.route)

    Scaffold(
        bottomBar = { AppBottomBar(navController) },
        floatingActionButton = {
            if (isHome) {
                FloatingActionButton(onClick = { navController.navigate(Screen.AddMeal.route) }) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter un repas")
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
        composable(Screen.Home.route) {
            val viewModel = viewModel { HomeViewModel() }
            HomeScreen(viewModel, navController)
        }

        composable(Screen.AddMeal.route) {
            AddMealScreen(navController)
        }

        composable(Screen.Plan.route) {
            PlanScreen(navController)
        }

        composable(Screen.Shopping.route) {
            ShoppingListScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(
            route = Screen.MealDetails.route,
            arguments = listOf(navArgument("mealId") { type = NavType.LongType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getLong("mealId") ?: 0
            MealDetailsScreen(mealId, navController)
        }

        composable(
            route = Screen.EditMeal.route,
            arguments = listOf(navArgument("mealId") { type = NavType.LongType })
        ) { backStackEntry ->
            val mealId = backStackEntry.arguments?.getLong("mealId") ?: 0
            EditMealScreen(mealId, navController)
        }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    val items = listOf(
        Triple(Screen.Home.route, "Repas", Icons.Default.Home),
        Triple(Screen.Plan.route, "Plan", Icons.Default.CalendarMonth),
        Triple(Screen.Shopping.route, "Courses", Icons.Default.ShoppingCart),
        Triple(Screen.Profile.route, "Profil", Icons.Default.Settings)
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topRoutes = items.map { it.first }.toSet()
    val show = currentDestination?.route in topRoutes || currentDestination.isInHierarchy(Screen.Home.route)
    if (show) {
        NavigationBar {
            items.forEach { (route, label, icon) ->
                val selected = currentDestination.isInHierarchy(route)
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(icon, contentDescription = label) },
                    label = { androidx.compose.material3.Text(label) }
                )
            }
        }
    }
}

private fun NavDestination?.isInHierarchy(route: String): Boolean = this?.hierarchy?.any { it.route == route } == true
