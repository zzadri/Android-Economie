package com.example.projet1diiage.home.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.projet1diiage.home.domain.model.Meal

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val ingredients: String // Stockés sous forme de chaîne séparée par des virgules
) {
    fun toDomain(): Meal {
        return Meal(
            id = id,
            title = title,
            ingredients = ingredients.split(",").filter { it.isNotBlank() }
        )
    }

    companion object {
        fun fromDomain(meal: Meal): MealEntity {
            return MealEntity(
                id = meal.id,
                title = meal.title,
                ingredients = meal.ingredients.joinToString(",")
            )
        }
    }
}
