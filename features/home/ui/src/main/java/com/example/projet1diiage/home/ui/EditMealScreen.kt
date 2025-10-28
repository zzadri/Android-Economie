package com.example.projet1diiage.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMealScreen(
    mealId: Long,
    navController: NavHostController,
    viewModel: EditMealViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = mealId) {
        viewModel.loadMeal(mealId)
    }

    val mealState by viewModel.meal.collectAsState()

    var title by remember { mutableStateOf("") }
    var currentIngredient by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf<List<String>>(emptyList()) }
    var showEmptyFieldsError by remember { mutableStateOf(false) }

    // Met à jour les états locaux lorsque le repas est chargé
    LaunchedEffect(mealState) {
        mealState?.let { meal ->
            title = meal.title
            ingredients = meal.ingredients
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Modifier le repas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (mealState == null) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            Content(
                modifier = Modifier.padding(paddingValues),
                title = title,
                onTitleChange = { title = it },
                currentIngredient = currentIngredient,
                onCurrentIngredientChange = { currentIngredient = it },
                ingredients = ingredients,
                onAddIngredient = {
                    if (currentIngredient.isNotBlank()) {
                        ingredients = ingredients + currentIngredient.trim()
                        currentIngredient = ""
                    }
                },
                onRemoveIngredient = { ingredient ->
                    ingredients = ingredients.filter { it != ingredient }
                },
                onSaveClick = {
                    if (title.isBlank() || ingredients.isEmpty()) {
                        showEmptyFieldsError = true
                    } else {
                        mealState?.let { meal ->
                            viewModel.updateMeal(
                                meal.copy(
                                    title = title.trim(),
                                    ingredients = ingredients
                                )
                            )
                            navController.popBackStack()
                        }
                    }
                },
                showEmptyFieldsError = showEmptyFieldsError
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    title: String,
    onTitleChange: (String) -> Unit,
    currentIngredient: String,
    onCurrentIngredientChange: (String) -> Unit,
    ingredients: List<String>,
    onAddIngredient: () -> Unit,
    onRemoveIngredient: (String) -> Unit,
    onSaveClick: () -> Unit,
    showEmptyFieldsError: Boolean
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Titre
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Titre du repas") },
            modifier = Modifier.fillMaxWidth(),
            isError = showEmptyFieldsError && title.isBlank(),
            supportingText = {
                if (showEmptyFieldsError && title.isBlank()) {
                    Text("Le titre ne peut pas être vide")
                }
            }
        )

        // Ajout d'ingrédients
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = currentIngredient,
                onValueChange = onCurrentIngredientChange,
                label = { Text("Ingrédient") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                trailingIcon = {
                    if (currentIngredient.isNotBlank()) {
                        IconButton(onClick = { onCurrentIngredientChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Effacer")
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = onAddIngredient, enabled = currentIngredient.isNotBlank()) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter ingrédient")
            }
        }

        // Erreur pour liste d'ingrédients vide
        if (showEmptyFieldsError && ingredients.isEmpty()) {
            Text(
                "Ajoutez au moins un ingrédient",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Liste des ingrédients
        Text("Ingrédients :", style = MaterialTheme.typography.titleMedium)
        if (ingredients.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ingredients.forEach { ingredient ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• $ingredient",
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onRemoveIngredient(ingredient) }) {
                            Icon(Icons.Default.Close, contentDescription = "Supprimer")
                        }
                    }
                }
            }
        } else {
            Text(
                "Aucun ingrédient ajouté",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bouton de sauvegarde
        Button(
            onClick = onSaveClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enregistrer les modifications")
        }
    }
}
