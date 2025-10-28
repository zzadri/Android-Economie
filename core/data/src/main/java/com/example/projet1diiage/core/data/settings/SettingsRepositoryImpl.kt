package com.example.projet1diiage.core.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.projet1diiage.core.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class SettingsRepositoryImpl(private val context: Context) : SettingsRepository {
    private val KEY_INCLUDE_WEEKEND = booleanPreferencesKey("include_weekend")

    override val includeWeekend: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[KEY_INCLUDE_WEEKEND] ?: false }

    override suspend fun setIncludeWeekend(value: Boolean) {
        context.dataStore.edit { it[KEY_INCLUDE_WEEKEND] = value }
    }
}

