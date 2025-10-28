package com.example.projet1diiage.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.repository.HomeRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddMealViewModel : ViewModel(), KoinComponent {
    private val repository: HomeRepository by inject()

    fun addMeal(meal: Meal) {
        viewModelScope.launch {
            repository.insertMeal(meal)
        }
    }
}
