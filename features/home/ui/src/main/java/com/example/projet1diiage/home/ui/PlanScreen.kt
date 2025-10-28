package com.example.projet1diiage.home.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.model.MealPlanEntry
import com.example.projet1diiage.home.domain.model.MealSlot
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanScreen(
    navController: NavHostController,
    viewModel: PlanViewModel = koinViewModel()
) {
    val weekStart by viewModel.weekStart.collectAsState()
    val weekPlan by viewModel.weekPlan.collectAsState()
    val meals by viewModel.allMeals.collectAsState(initial = emptyList())
    val includeWeekend by viewModel.includeWeekend.collectAsState(initial = false)

    var includeLunch by remember { mutableStateOf(true) }
    var includeDinner by remember { mutableStateOf(true) }
    var showWeekPicker by remember { mutableStateOf(false) }
    var dayMode by remember { mutableStateOf(false) } // false = semaine, true = jour
    var selectedDayOffset by remember(weekStart) {
        mutableStateOf((LocalDate.now().dayOfWeek.value - java.time.DayOfWeek.MONDAY.value).coerceIn(0, 6))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plan des repas") },
                actions = {
                    IconButton(onClick = { viewModel.goToToday() }) {
                        Icon(Icons.Default.Today, contentDescription = "Aujourd'hui")
                    }
                    IconButton(onClick = { showWeekPicker = true }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Choisir semaine")
                    }
                    IconButton(onClick = { viewModel.generateForCurrentMonth(includeLunch, includeDinner) }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Générer mois")
                    }
                    IconButton(onClick = { viewModel.randomizeWeek() }) {
                        Icon(Icons.Default.Shuffle, contentDescription = "Randomiser semaine")
                    }
                }
            )
        }
    ) { padding ->
        Content(
            modifier = Modifier.padding(padding),
            weekStart = weekStart,
            weekPlan = weekPlan,
            meals = meals,
            includeWeekend = includeWeekend,
            includeLunch = includeLunch,
            includeDinner = includeDinner,
            onToggleLunch = { includeLunch = it },
            onToggleDinner = { includeDinner = it },
            onPrevWeek = { viewModel.prevWeek() },
            onNextWeek = { viewModel.nextWeek() },
            onAssign = { date, slot, mealId -> viewModel.assign(date, slot, mealId) },
            onClear = { date, slot -> viewModel.clear(date, slot) },
            dayMode = dayMode,
            onToggleMode = { dayMode = it },
            selectedDayOffset = selectedDayOffset,
            onSelectDayOffset = { selectedDayOffset = it }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberDatePickerState(initialSelectedDateMillis: Long?): DatePickerState =
    androidx.compose.material3.rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)

private fun LocalDate.toEpochMillis(): Long =
    this.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()

private fun Long.toLocalDate(): LocalDate =
    java.time.Instant.ofEpochMilli(this).atZone(java.time.ZoneId.systemDefault()).toLocalDate()

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    weekStart: LocalDate,
    weekPlan: List<MealPlanEntry>,
    meals: List<Meal>,
    includeWeekend: Boolean,
    includeLunch: Boolean,
    includeDinner: Boolean,
    onToggleLunch: (Boolean) -> Unit,
    onToggleDinner: (Boolean) -> Unit,
    onPrevWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onAssign: (LocalDate, MealSlot, Long) -> Unit,
    onClear: (LocalDate, MealSlot) -> Unit,
    dayMode: Boolean,
    onToggleMode: (Boolean) -> Unit,
    selectedDayOffset: Int,
    onSelectDayOffset: (Int) -> Unit
) {
    val dfCompact = remember { DateTimeFormatter.ofPattern("d MMM", Locale.getDefault()) }
    val weekEnd = remember(weekStart, includeWeekend) { weekStart.plusDays(if (includeWeekend) 6 else 4) }
    val weekNum = remember(weekStart) { weekStart.get(WeekFields.ISO.weekOfWeekBasedYear()) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        WeekHeader(
            label = "S $weekNum · ${weekStart.format(dfCompact)} – ${weekEnd.format(dfCompact)}",
            onPrev = onPrevWeek,
            onNext = onNextWeek
        )

        Spacer(Modifier.height(12.dp))
        // Jour | Semaine segmented switch
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = !dayMode,
                onClick = { onToggleMode(false) },
                shape = SegmentedButtonDefaults.itemShape(0, 2),
                label = { Text("Semaine") }
            )
            SegmentedButton(
                selected = dayMode,
                onClick = { onToggleMode(true) },
                shape = SegmentedButtonDefaults.itemShape(1, 2),
                label = { Text("Jour") }
            )
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = includeLunch, onClick = { onToggleLunch(!includeLunch) }, label = { Text("Midi") })
            FilterChip(selected = includeDinner, onClick = { onToggleDinner(!includeDinner) }, label = { Text("Soir") })
        }

        Spacer(Modifier.height(12.dp))

        val entriesByDateSlot = weekPlan.associateBy { it.date to it.slot }
        val maxOffset = if (includeWeekend) 6 else 4
        if (dayMode) {
            DaySelector(
                weekStart = weekStart,
                includeWeekend = includeWeekend,
                entriesByDateSlot = entriesByDateSlot,
                selectedOffset = selectedDayOffset,
                onSelect = onSelectDayOffset
            )

            Spacer(Modifier.height(12.dp))
            val date = weekStart.plusDays(selectedDayOffset.coerceIn(0, maxOffset).toLong())
            DayCard(
                date = date,
                entryLunch = entriesByDateSlot[date to MealSlot.LUNCH],
                entryDinner = entriesByDateSlot[date to MealSlot.DINNER],
                meals = meals,
                showLunch = includeLunch,
                showDinner = includeDinner,
                onAssign = onAssign,
                onClear = onClear
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items((0..maxOffset).toList()) { i ->
                    val date = weekStart.plusDays(i.toLong())
                    DayCard(
                        date = date,
                        entryLunch = entriesByDateSlot[date to MealSlot.LUNCH],
                        entryDinner = entriesByDateSlot[date to MealSlot.DINNER],
                        meals = meals,
                        showLunch = includeLunch,
                        showDinner = includeDinner,
                        onAssign = onAssign,
                        onClear = onClear
                    )
                }
            }
        }
    }
}

@Composable
private fun DayCard(
    date: LocalDate,
    entryLunch: MealPlanEntry?,
    entryDinner: MealPlanEntry?,
    meals: List<Meal>,
    showLunch: Boolean,
    showDinner: Boolean,
    onAssign: (LocalDate, MealSlot, Long) -> Unit,
    onClear: (LocalDate, MealSlot) -> Unit
) {
    val dayLabel = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("$dayLabel ${date.dayOfMonth}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (showLunch) MealSlotRow(date, MealSlot.LUNCH, entryLunch?.mealId, meals, onAssign, onClear)
                if (showDinner) MealSlotRow(date, MealSlot.DINNER, entryDinner?.mealId, meals, onAssign, onClear)
            }
        }
    }
}

@Composable
private fun MealSlotRow(
    date: LocalDate,
    slot: MealSlot,
    currentMealId: Long?,
    meals: List<Meal>,
    onAssign: (LocalDate, MealSlot, Long) -> Unit,
    onClear: (LocalDate, MealSlot) -> Unit
) {
    var open by remember { mutableStateOf(false) }
    val currentName = meals.firstOrNull { it.id == currentMealId }?.title
    Card(
        onClick = { open = true },
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.elevatedCardColors()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val icon = if (slot == MealSlot.LUNCH) Icons.Outlined.WbSunny else Icons.Outlined.NightsStay
            Icon(icon, contentDescription = null)
            Column(Modifier.weight(1f)) {
                Text(if (slot == MealSlot.LUNCH) "Midi" else "Soir", style = MaterialTheme.typography.labelLarge)
                Text(currentName ?: "Ajouter...", style = MaterialTheme.typography.bodyMedium)
            }
            Icon(Icons.Default.Add, contentDescription = null)
        }
    }
    DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
        meals.forEach { meal ->
            DropdownMenuItem(
                text = { Text(meal.title) },
                onClick = {
                    onAssign(date, slot, meal.id)
                    open = false
                }
            )
        }
        if (currentMealId != null) {
            DropdownMenuItem(
                text = { Text("Retirer") },
                onClick = {
                    onClear(date, slot)
                    open = false
                }
            )
        }
    }
}

@Composable
private fun WeekHeader(label: String, onPrev: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrev) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Semaine précédente") }
        Text(label, style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = onNext) { Icon(Icons.Filled.ArrowForward, contentDescription = "Semaine suivante") }
    }
}

@Composable
private fun DaySelector(
    weekStart: LocalDate,
    includeWeekend: Boolean,
    entriesByDateSlot: Map<Pair<LocalDate, MealSlot>, MealPlanEntry?>,
    selectedOffset: Int,
    onSelect: (Int) -> Unit
) {
    val maxOffset = if (includeWeekend) 6 else 4
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed((0..maxOffset).toList()) { index, _ ->
            val date = weekStart.plusDays(index.toLong())
            val hasMeal = entriesByDateSlot[date to MealSlot.LUNCH]?.mealId != null ||
                entriesByDateSlot[date to MealSlot.DINNER]?.mealId != null
            FilterChip(
                selected = selectedOffset == index,
                onClick = { onSelect(index) },
                label = {
                    val text = "${date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ${date.dayOfMonth}"
                    Text(text)
                },
                trailingIcon = if (hasMeal) ({ Text("•") }) else null
            )
        }
    }
}

