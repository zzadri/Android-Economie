package com.example.projet1diiage.profile.api

import com.example.projet1diiage.core.data.settings.SettingsRepositoryImpl
import com.example.projet1diiage.core.domain.settings.SettingsRepository
import com.example.projet1diiage.profile.ui.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ProfileModule = module {
    // Settings binding lives with the profile feature (composition root loads all modules)
    single<SettingsRepository> { SettingsRepositoryImpl(androidContext()) }

    viewModel { ProfileViewModel(get()) }
}

