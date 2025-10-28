package com.example.projet1diiage.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet1diiage.home.domain.repository.PlanningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class ShoppingListViewModel(
    private val planningRepository: PlanningRepository
) : ViewModel() {

    private val _weekStart = MutableStateFlow(currentWeekStart(LocalDate.now()))
    val weekStart: StateFlow<LocalDate> = _weekStart.asStateFlow()

    private val _items = MutableStateFlow<List<String>>(emptyList())
    val items: StateFlow<List<String>> = _items.asStateFlow()

    init { refresh() }

    fun nextWeek() { _weekStart.value = _weekStart.value.plusWeeks(1); refresh() }
    fun prevWeek() { _weekStart.value = _weekStart.value.minusWeeks(1); refresh() }

    fun refresh() {
        viewModelScope.launch {
            _items.value = planningRepository.getShoppingListForWeek(_weekStart.value)
        }
    }

    fun setWeekStart(date: LocalDate) {
        _weekStart.value = currentWeekStart(date)
        refresh()
    }

    private fun currentWeekStart(date: LocalDate): LocalDate {
        var d = date
        while (d.dayOfWeek != DayOfWeek.MONDAY) d = d.minusDays(1)
        return d
    }
}
