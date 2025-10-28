package com.example.projet1diiage.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projet1diiage.core.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val includeWeekend: StateFlow<Boolean> = settingsRepository.includeWeekend
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun setIncludeWeekend(value: Boolean) {
        viewModelScope.launch { settingsRepository.setIncludeWeekend(value) }
    }
}

