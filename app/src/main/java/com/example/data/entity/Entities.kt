package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recipes")
@Serializable
data class Recipe(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAr: String,
    val nameLocal: String?,
    val countryId: String,
    val category: String, // breakfast, lunch, etc.
    val cuisineType: String,
    val history: String,
    val imageUrl: String,
    val preparationTime: Int, // in minutes
    val cookingTime: Int, // in minutes
    val difficulty: String, // Easy, Medium, Hard
    val calories: Int,
    val servings: Int,
    val nutritionFacts: String, // JSON or formatted string
    val allergens: String, // Comma separated
    val chefTips: String,
    val videoUrl: String? = null,
    val isFavorite: Boolean = false
)

@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: String,
    val name: String,
    val amount: String,
    val unit: String
)

@Entity(tableName = "instructions")
data class Instruction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeId: String,
    val stepNumber: Int,
    val description: String
)

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAr: String,
    val continent: String,
    val flagUrl: String,
    val description: String
)

@Entity(tableName = "cities")
data class City(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAr: String,
    val countryId: String,
    val description: String,
    val imageUrl: String? = null
)

@Entity(tableName = "user_achievements")
data class Achievement(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconResId: Int,
    val isUnlocked: Boolean = false,
    val progress: Int = 0
)
