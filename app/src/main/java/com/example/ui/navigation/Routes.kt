package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
object ExploreRoute

@Serializable
data class RecipeDetailRoute(val recipeId: String)

@Serializable
object AiChefRoute

@Serializable
object AcademyRoute

@Serializable
object ProfileRoute

@Serializable
object FavoritesRoute
