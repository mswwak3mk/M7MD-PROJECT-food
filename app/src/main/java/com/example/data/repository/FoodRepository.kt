package com.example.data.repository

import com.example.data.dao.*
import com.example.data.entity.*
import kotlinx.coroutines.flow.Flow

class FoodRepository(
    private val recipeDao: RecipeDao,
    private val countryDao: CountryDao,
    private val cityDao: CityDao,
    private val achievementDao: AchievementDao
) {
    val allRecipes: Flow<List<Recipe>> = recipeDao.getAllRecipes()
    val allCountries: Flow<List<Country>> = countryDao.getAllCountries()
    val allCities: Flow<List<City>> = cityDao.getAllCities()
    val allAchievements: Flow<List<Achievement>> = achievementDao.getAllAchievements()
    val favoriteRecipes: Flow<List<Recipe>> = recipeDao.getFavoriteRecipes()

    fun getRecipesByCountry(countryId: String) = recipeDao.getRecipesByCountry(countryId)
    fun getRecipesByCategory(category: String) = recipeDao.getRecipesByCategory(category)
    fun getIngredientsForRecipe(recipeId: String) = recipeDao.getIngredientsForRecipe(recipeId)
    fun getInstructionsForRecipe(recipeId: String) = recipeDao.getInstructionsForRecipe(recipeId)
    fun searchRecipes(query: String) = recipeDao.searchRecipes(query)
    fun searchCities(query: String) = cityDao.searchCities(query)

    suspend fun getRecipeById(id: String) = recipeDao.getRecipeById(id)
    suspend fun toggleFavorite(recipe: Recipe) {
        recipeDao.updateRecipe(recipe.copy(isFavorite = !recipe.isFavorite))
    }

    suspend fun insertRecipe(recipe: Recipe, ingredients: List<Ingredient>, instructions: List<Instruction>) {
        recipeDao.insertRecipe(recipe)
        recipeDao.insertIngredients(ingredients)
        recipeDao.insertInstructions(instructions)
    }

    suspend fun insertCountry(country: Country) = countryDao.insertCountry(country)
    suspend fun insertCity(city: City) = cityDao.insertCity(city)
}
