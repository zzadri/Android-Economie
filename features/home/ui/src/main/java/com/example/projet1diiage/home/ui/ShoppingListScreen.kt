package com.example.projet1diiage.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    navController: NavHostController,
    viewModel: ShoppingListViewModel = koinViewModel()
) {
    val weekStart by viewModel.weekStart.collectAsState()
    val items by viewModel.items.collectAsState()
    val df = remember { DateTimeFormatter.ofPattern("dd/MM") }
    val clipboard = LocalClipboardManager.current
    var showWeekPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liste de courses (${weekStart.format(df)})") },
                actions = {
                    IconButton(onClick = { showWeekPicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Choisir semaine")
                    }
                    IconButton(onClick = {
                        val header = "Voici les ingrédients dont j’ai besoin pour cette semaine :"
                        val lines = items.map { "- $it" }
                        val text = (sequenceOf(header) + lines).joinToString("\n")
                        clipboard.setText(AnnotatedString(text))
                    }) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copier")
                    }
                }
            )
        }
    ) { padding ->
        Content(
            modifier = Modifier.padding(padding),
            items = items,
            onPrev = { viewModel.prevWeek() },
            onNext = { viewModel.nextWeek() }
        )
    }

    if (showWeekPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = weekStart.toEpochMillis())
        DatePickerDialog(
            onDismissRequest = { showWeekPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val millis = state.selectedDateMillis
                    if (millis != null) viewModel.setWeekStart(millis.toLocalDate())
                    showWeekPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showWeekPicker = false }) { Text("Annuler") } }
        ) {
            DatePicker(state = state)
        }
    }
}

private fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    java.time.Instant.ofEpochMilli(this).atZone(java.time.ZoneId.systemDefault()).toLocalDate()

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    items: List<String>,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onPrev) { Text("Semaine préc.") }
            OutlinedButton(onClick = onNext) { Text("Semaine suiv.") }
        }

        Spacer(Modifier.height(16.dp))
        if (items.isEmpty()) {
            Text("Aucun ingrédient pour cette semaine.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { item ->
                    Text("- $item", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
