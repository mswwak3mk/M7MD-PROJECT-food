package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.entity.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onRecipeClick: (String) -> Unit
) {
    val recipes by viewModel.allRecipes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Text(
                text = "Discover World Flavors",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search recipes or ingredients...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (searchQuery.isNotBlank()) {
            items(searchResults) { recipe ->
                RecipeListItem(recipe, onRecipeClick)
            }
        } else {
            item {
                SectionHeader("Featured Recipes")
                FeaturedRecipesRow(recipes, onRecipeClick)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                SectionHeader("Popular Categories")
                CategoryChips()
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(recipes) { recipe ->
                RecipeCard(recipe, onRecipeClick)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun FeaturedRecipesRow(recipes: List<Recipe>, onRecipeClick: (String) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(recipes.take(5)) { recipe ->
            Box(
                modifier = Modifier
                    .width(280.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onRecipeClick(recipe.id) }
            ) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.nameEn,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(recipe.nameEn, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text(recipe.cuisineType, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(recipe.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(recipe.nameEn, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${recipe.calories} kcal", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Translate, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(recipe.nameAr, style = MaterialTheme.typography.bodyLarge)
                }
                Text(
                    text = "${recipe.cookingTime + recipe.preparationTime} min • ${recipe.difficulty}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryChips() {
    val categories = listOf("Breakfast", "Lunch", "Dinner", "Dessert", "Drinks", "Street Food", "Vegetarian")
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categories.forEach { category ->
            FilterChip(
                selected = false,
                onClick = { },
                label = { Text(category) }
            )
        }
    }
}

@Composable
fun RecipeListItem(recipe: Recipe, onClick: (String) -> Unit) {
    ListItem(
        headlineContent = { Text(recipe.nameEn) },
        supportingContent = { Text(recipe.cuisineType) },
        leadingContent = {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        },
        modifier = Modifier.clickable { onClick(recipe.id) }
    )
}
