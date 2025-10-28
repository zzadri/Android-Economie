package com.example.projet1diiage.home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel, NavHost: NavHostController) {
    val meals by viewModel.meals.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repas") }
            )
        }
    ) { paddingValues ->
        Content(
            modifier = Modifier.padding(paddingValues),
            meals = meals,
            onMealClick = { mealId -> NavHost.navigate(Screen.MealDetails.createRoute(mealId)) },
            onEditClick = { mealId -> NavHost.navigate(Screen.EditMeal.createRoute(mealId)) },
            onDeleteClick = { mealId -> viewModel.deleteMeal(mealId) }
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    meals: List<Meal>,
    onMealClick: (Long) -> Unit,
    onEditClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (meals.isEmpty()) {
            EmptyMealsState()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(meals) { meal ->
                    MealItem(
                        meal = meal,
                        onClick = { onMealClick(meal.id) },
                        onEditClick = { onEditClick(meal.id) },
                        onDeleteClick = { onDeleteClick(meal.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun MealItem(
    meal: Meal,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = meal.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Modifier")
                    }
                    IconButton(onClick = onDeleteClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Supprimer")
                    }
                }
            }

            val count = meal.ingredients.size
            Spacer(Modifier.height(6.dp))
            Text(
                text = if (count == 0) "Aucun ingrédient" else if (count == 1) "1 ingrédient" else "$count ingrédients",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyMealsState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Aucun repas enregistré",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Ajoutez votre premier repas avec le bouton +",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
