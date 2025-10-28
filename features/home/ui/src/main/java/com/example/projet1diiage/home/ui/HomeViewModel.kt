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

class HomeViewModel() : ViewModel(), KoinComponent {

    private val _meals = MutableStateFlow<List<Meal>>(emptyList())
    val meals: StateFlow<List<Meal>> = _meals.asStateFlow()

    private val repository: HomeRepository by inject()

    init {
        loadMeals()
    }

    private fun loadMeals() {
        viewModelScope.launch {
            repository.getAllMeals().collect { mealsList ->
                _meals.value = mealsList
            }
        }
    }

    fun deleteMeal(mealId: Long) {
        viewModelScope.launch {
            repository.deleteMeal(mealId)
        }
    }
}
