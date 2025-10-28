package com.example.projet1diiage.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MealDetailsViewModel : ViewModel(), KoinComponent {
    private val repository: HomeRepository by inject()

    private val _meal = MutableStateFlow<Meal?>(null)
    val meal: StateFlow<Meal?> = _meal.asStateFlow()

    fun loadMeal(id: Long) {
        viewModelScope.launch {
            _meal.value = repository.getMeal(id)
        }
    }
}
