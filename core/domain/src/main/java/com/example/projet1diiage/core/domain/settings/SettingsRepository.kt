package com.example.projet1diiage.core.domain.settings

import kotlinx.coroutines.flow.Flow

/**
 * Central user settings used across features.
 * For now we only expose whether weekend days should be included
 * in planning (UI display and auto-generation/randomization).
 */
interface SettingsRepository {
    /** When true, show Saturday/Sunday and include them in generation. */
    val includeWeekend: Flow<Boolean>

    suspend fun setIncludeWeekend(value: Boolean)
}

