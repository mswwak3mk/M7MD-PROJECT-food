package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val recipes by viewModel.allRecipes.collectAsState()
    val recipe = recipes.find { it.id == recipeId }

    recipe?.let { r ->
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(r.nameEn) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleFavorite(r) }) {
                            Icon(
                                if (r.isFavorite) Icons.Default.Star else Icons.Default.Star,
                                contentDescription = "Favorite",
                                tint = if (r.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                item {
                    AsyncImage(
                        model = r.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = r.nameAr,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            Badge(containerColor = MaterialTheme.colorScheme.secondaryContainer) {
                                Text(r.difficulty, modifier = Modifier.padding(4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Origin: ${r.countryId.uppercase()}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            InfoItem(Icons.Default.Schedule, "${r.cookingTime + r.preparationTime}m")
                            InfoItem(Icons.Default.Star, r.cuisineType)
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        
                        Text("History & Culture", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(r.history, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(vertical = 8.dp))
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                        
                        Text("Ingredients", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        // In a real app, these would come from the repository
                        val mockIngredients = listOf("High quality meat", "Traditional spices", "Local vegetable selection")
                        Column {
                            mockIngredients.forEach { ingredient ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("• $ingredient", modifier = Modifier.weight(1f))
                                    TextButton(onClick = { viewModel.addToShoppingList(ingredient) }) {
                                        Text("Add")
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Instructions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("1. Prepare ingredients carefully.\n2. Follow traditional methods strictly.\n3. Serve warm and enjoy with family.", modifier = Modifier.padding(vertical = 8.dp))
                        
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}
