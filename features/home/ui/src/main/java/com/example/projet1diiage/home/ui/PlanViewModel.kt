package com.example.projet1diiage.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.model.MealPlanEntry
import com.example.projet1diiage.home.domain.model.MealSlot
import com.example.projet1diiage.home.domain.repository.HomeRepository
import com.example.projet1diiage.home.domain.repository.PlanningRepository
import com.example.projet1diiage.core.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class PlanViewModel(
    private val planningRepository: PlanningRepository,
    private val homeRepository: HomeRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _weekStart = MutableStateFlow(currentWeekStart(LocalDate.now()))
    val weekStart: StateFlow<LocalDate> = _weekStart.asStateFlow()

    val weekPlan = _weekStart.flatMapLatest { start ->
        planningRepository.getWeekPlan(start)
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, emptyList())

    val allMeals = homeRepository.getAllMeals()

    // Whether to include weekend in planning and UI (from settings)
    val includeWeekend = settingsRepository.includeWeekend

    fun nextWeek() { _weekStart.value = _weekStart.value.plusWeeks(1) }
    fun prevWeek() { _weekStart.value = _weekStart.value.minusWeeks(1) }

    fun generateForCurrentMonth(includeLunch: Boolean, includeDinner: Boolean) {
        val now = LocalDate.now()
        viewModelScope.launch {
            planningRepository.generateMonthlyPlan(
                month = now.monthValue,
                year = now.year,
                includeLunch = includeLunch,
                includeDinner = includeDinner,
                includeWeekend = includeWeekend.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, false).value
            )
        }
    }

    fun assign(date: LocalDate, slot: MealSlot, mealId: Long) {
        viewModelScope.launch { planningRepository.assignMeal(date, slot, mealId) }
    }

    fun clear(date: LocalDate, slot: MealSlot) {
        viewModelScope.launch { planningRepository.clearAssignment(date, slot) }
    }

    fun randomizeWeek() {
        viewModelScope.launch {
            val withWkd = includeWeekend.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, false).value
            planningRepository.randomizeWeek(_weekStart.value, withWkd)
        }
    }

    fun setWeekStart(date: LocalDate) {
        _weekStart.value = currentWeekStart(date)
    }

    fun goToToday() {
        _weekStart.value = currentWeekStart(LocalDate.now())
    }

    private fun currentWeekStart(date: LocalDate): LocalDate {
        var d = date
        while (d.dayOfWeek != DayOfWeek.MONDAY) d = d.minusDays(1)
        return d
    }
}
