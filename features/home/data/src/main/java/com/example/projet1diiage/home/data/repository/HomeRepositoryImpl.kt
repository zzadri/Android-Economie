package com.example.projet1diiage.home.data.repository

import com.example.projet1diiage.home.data.local.MealDao
import com.example.projet1diiage.home.data.local.MealEntity
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val mealDao: MealDao
) : HomeRepository {

    override fun getAllMeals(): Flow<List<Meal>> {
        return mealDao.getAllMeals().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMeal(id: Long): Meal? {
        return mealDao.getMealById(id)?.toDomain()
    }

    override suspend fun insertMeal(meal: Meal): Long {
        return mealDao.insertMeal(MealEntity.fromDomain(meal))
    }

    override suspend fun updateMeal(meal: Meal) {
        mealDao.updateMeal(MealEntity.fromDomain(meal))
    }

    override suspend fun deleteMeal(id: Long) {
        mealDao.deleteMeal(id)
    }
}
