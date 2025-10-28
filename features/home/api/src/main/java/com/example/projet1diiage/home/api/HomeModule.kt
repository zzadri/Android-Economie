package com.example.projet1diiage.home.api

import androidx.room.Room
import com.example.projet1diiage.home.data.local.AppDatabase
import com.example.projet1diiage.home.data.repository.HomeRepositoryImpl
import com.example.projet1diiage.home.data.repository.PlanningRepositoryImpl
import com.example.projet1diiage.home.domain.repository.HomeRepository
import com.example.projet1diiage.home.domain.repository.PlanningRepository
import com.example.projet1diiage.home.ui.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val HomeModule = module {
    // Base de données
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "economie-app-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    // DAO
    single { get<AppDatabase>().mealDao() }
    single { get<AppDatabase>().mealPlanDao() }

    // Repository
    single<HomeRepository> { HomeRepositoryImpl(get()) }
    single<PlanningRepository> { PlanningRepositoryImpl(get(), get()) }

    // ViewModels
    viewModel { HomeViewModel() }
    viewModel { AddMealViewModel() }
    viewModel { MealDetailsViewModel() }
    viewModel { EditMealViewModel() }
    viewModel { PlanViewModel(get(), get(), get()) }
    viewModel { ShoppingListViewModel(get()) }
}
