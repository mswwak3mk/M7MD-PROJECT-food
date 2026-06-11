package com.example.data.dao

import androidx.room.*
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipeById(id: String): Recipe?

    @Query("SELECT * FROM recipes WHERE countryId = :countryId")
    fun getRecipesByCountry(countryId: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE category = :category")
    fun getRecipesByCategory(category: String): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE nameEn LIKE '%' || :query || '%' OR nameAr LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Transaction
    @Query("SELECT * FROM ingredients WHERE recipeId = :recipeId")
    fun getIngredientsForRecipe(recipeId: String): Flow<List<Ingredient>>

    @Transaction
    @Query("SELECT * FROM instructions WHERE recipeId = :recipeId ORDER BY stepNumber ASC")
    fun getInstructionsForRecipe(recipeId: String): Flow<List<Instruction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<Ingredient>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstructions(instructions: List<Instruction>)
}

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<Country>>

    @Query("SELECT * FROM countries WHERE continent = :continent")
    fun getCountriesByContinent(continent: String): Flow<List<Country>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: Country)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM user_achievements")
    fun getAllAchievements(): Flow<List<Achievement>>

    @Update
    suspend fun updateAchievement(achievement: Achievement)
}
