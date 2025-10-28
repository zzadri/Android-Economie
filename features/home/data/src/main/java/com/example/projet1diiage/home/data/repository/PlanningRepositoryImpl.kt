package com.example.projet1diiage.home.data.repository

import com.example.projet1diiage.home.data.local.MealDao
import com.example.projet1diiage.home.data.local.MealPlanDao
import com.example.projet1diiage.home.data.local.MealPlanEntity
import com.example.projet1diiage.home.domain.model.Meal
import com.example.projet1diiage.home.domain.model.MealPlanEntry
import com.example.projet1diiage.home.domain.model.MealSlot
import com.example.projet1diiage.home.domain.repository.PlanningRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class PlanningRepositoryImpl(
    private val mealPlanDao: MealPlanDao,
    private val mealDao: MealDao
) : PlanningRepository {

    override suspend fun generateMonthlyPlan(
        month: Int,
        year: Int,
        includeLunch: Boolean,
        includeDinner: Boolean,
        includeWeekend: Boolean
    ) {
        val ym = YearMonth.of(year, month)
        val start = ym.atDay(1)
        val end = ym.atEndOfMonth()
        var date = start
        while (!date.isAfter(end)) {
            val isWeekday = date.dayOfWeek.value in DayOfWeek.MONDAY.value..DayOfWeek.FRIDAY.value
            val isWeekend = date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY
            if (isWeekday || (includeWeekend && isWeekend)) {
                if (includeLunch) {
                    mealPlanDao.insert(
                        MealPlanEntity(
                            date = date.toString(),
                            slot = MealSlot.LUNCH.name,
                            mealId = null
                        )
                    )
                }
                if (includeDinner) {
                    mealPlanDao.insert(
                        MealPlanEntity(
                            date = date.toString(),
                            slot = MealSlot.DINNER.name,
                            mealId = null
                        )
                    )
                }
            }
            date = date.plusDays(1)
        }
    }

    override fun getWeekPlan(weekStart: LocalDate): Flow<List<MealPlanEntry>> {
        val start = weekStart
        val end = weekStart.plusDays(6)
        return mealPlanDao.getBetween(start.toString(), end.toString()).map { list ->
            list.map {
                MealPlanEntry(
                    id = it.id,
                    date = LocalDate.parse(it.date),
                    slot = MealSlot.valueOf(it.slot),
                    mealId = it.mealId
                )
            }
        }
    }

    override suspend fun assignMeal(date: LocalDate, slot: MealSlot, mealId: Long) {
        val existing = mealPlanDao.getByDateAndSlot(date.toString(), slot.name)
        if (existing != null) {
            mealPlanDao.update(existing.copy(mealId = mealId))
        } else {
            mealPlanDao.insert(
                MealPlanEntity(date = date.toString(), slot = slot.name, mealId = mealId)
            )
        }
    }

    override suspend fun clearAssignment(date: LocalDate, slot: MealSlot) {
        val existing = mealPlanDao.getByDateAndSlot(date.toString(), slot.name)
        if (existing != null) {
            mealPlanDao.update(existing.copy(mealId = null))
        } else {
            // Ensure there is an entry so UI reflects unassigned state
            mealPlanDao.insert(
                MealPlanEntity(date = date.toString(), slot = slot.name, mealId = null)
            )
        }
    }

    override suspend fun getMealsForEntries(entries: List<MealPlanEntry>): List<Meal> {
        val ids = entries.mapNotNull { it.mealId }.distinct()
        // naive: fetch individually using MealDao since no batch method available
        return ids.mapNotNull { id -> mealDao.getMealById(id)?.toDomain() }
    }

    override suspend fun getShoppingListForWeek(weekStart: LocalDate): List<String> {
        val entries = getWeekPlan(weekStart)
        // Collect once by reading underlying DAO directly (simplify with blocking call avoided);
        // we will compute by querying for dates and mapping.
        val start = weekStart
        val end = weekStart.plusDays(6)
        val list = mealPlanDao.getBetween(start.toString(), end.toString())
        // convert flow to one-shot list by first() would require coroutines; avoid complexity by
        // providing a simple aggregation using existing DAO methods through a temporary map.
        // However Flow requires collection; to keep API, we re-query synchronously via getByDateAndSlot over range.
        val aggregated = mutableListOf<String>()
        var date = start
        while (!date.isAfter(end)) {
            for (slot in listOf(MealSlot.LUNCH, MealSlot.DINNER)) {
                val e = mealPlanDao.getByDateAndSlot(date.toString(), slot.name)
                if (e?.mealId != null) {
                    val meal = mealDao.getMealById(e.mealId)
                    if (meal != null) {
                        val ing = meal.ingredients.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        aggregated.addAll(ing)
                    }
                }
            }
            date = date.plusDays(1)
        }
        return aggregated.map { it.trim() }.filter { it.isNotEmpty() }.distinct().sorted()
    }

    override suspend fun randomizeWeek(weekStart: LocalDate, includeWeekend: Boolean) {
        val start = weekStart
        val end = if (includeWeekend) weekStart.plusDays(6) else weekStart.plusDays(4)
        val entries = mealPlanDao.getBetween(start.toString(), end.toString()).first()
        if (entries.isEmpty()) return

        val meals = mealDao.getAllMeals().first()
        if (meals.isEmpty()) return

        val ids = meals.map { it.id }
        val pool = mutableListOf<Long>()
        while (pool.size < entries.size) {
            pool.addAll(ids.shuffled())
        }

        entries.sortedWith(compareBy({ it.date }, { it.slot })).forEachIndexed { index, entry ->
            val assignedId = pool[index]
            mealPlanDao.update(entry.copy(mealId = assignedId))
        }
    }
}
