package com.example.projet1diiage.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailsScreen(
    mealId: Long,
    navController: NavHostController,
    viewModel: MealDetailsViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = mealId) {
        viewModel.loadMeal(mealId)
    }

    val mealState by viewModel.meal.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(mealState?.title ?: "Détails du repas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    mealState?.let { meal ->
                        IconButton(onClick = { navController.navigate(Screen.EditMeal.createRoute(meal.id)) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            meal = mealState
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    meal: Meal?
) {
    if (meal == null) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(androidx.compose.ui.Alignment.Center))
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = meal.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            item {
                Text(
                    text = "Ingrédients",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            items(meal.ingredients) { ingredient ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "• $ingredient",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
