package com.example.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import com.example.data.local.AppDatabase
import com.example.data.repository.FoodRepository
import com.example.ui.navigation.*
import com.example.ui.theme.WorldFoodTheme

@Composable
fun WorldFoodApp() {
    val context = LocalContext.current
    val database = remember { AppDatabase.getDatabase(context) }
    val repository = remember { FoodRepository(database.recipeDao(), database.countryDao(), database.achievementDao()) }
    val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(repository))

    // Initialize data
    LaunchedEffect(Unit) {
        viewModel.initMockData()
    }

    val navController = rememberNavController()

    WorldFoodTheme {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, "Home") },
                        label = { Text("Home") },
                        selected = currentDestination?.route?.contains("Home") == true,
                        onClick = { navController.navigate(HomeRoute) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Public, "Explore") },
                        label = { Text("Explore") },
                        selected = currentDestination?.route?.contains("Explore") == true,
                        onClick = { navController.navigate(ExploreRoute) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Restaurant, "Chef") },
                        label = { Text("AI Chef") },
                        selected = currentDestination?.route?.contains("AiChef") == true,
                        onClick = { navController.navigate(AiChefRoute) }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.School, "Academy") },
                        label = { Text("Academy") },
                        selected = currentDestination?.route?.contains("Academy") == true,
                        onClick = { navController.navigate(AcademyRoute) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeRoute,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<HomeRoute> { 
                    HomeScreen(viewModel, onRecipeClick = { id -> navController.navigate(RecipeDetailRoute(id)) }) 
                }
                composable<ExploreRoute> { 
                    ExploreScreen(viewModel, onCountryClick = { /* Filter home or navigate */ }) 
                }
                composable<AiChefRoute> { 
                    AiChefScreen(viewModel) 
                }
                composable<AcademyRoute> { 
                    AcademyScreen(viewModel) 
                }
                composable<RecipeDetailRoute> { backStackEntry ->
                    val route: RecipeDetailRoute = backStackEntry.toRoute()
                    RecipeDetailScreen(route.recipeId, viewModel, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}
