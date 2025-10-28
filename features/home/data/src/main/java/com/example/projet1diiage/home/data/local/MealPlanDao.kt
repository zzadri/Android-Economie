package com.example.projet1diiage.home.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// Etape 3 : créer le DAO (Data Access Object) pour accéder à la BDD
// en gros c'est une interface qui fais nos query SQL

@Dao
interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MealPlanEntity): Long

    @Update
    suspend fun update(entry: MealPlanEntity)

    @Query("SELECT * FROM meal_plan WHERE date BETWEEN :start AND :end ORDER BY date, slot")
    fun getBetween(start: String, end: String): Flow<List<MealPlanEntity>>

    @Query("SELECT * FROM meal_plan WHERE date = :date AND slot = :slot LIMIT 1")
    suspend fun getByDateAndSlot(date: String, slot: String): MealPlanEntity?

    @Query("DELETE FROM meal_plan")
    suspend fun clearAll()
}

