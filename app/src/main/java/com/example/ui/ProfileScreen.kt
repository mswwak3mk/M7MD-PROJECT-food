package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(viewModel: MainViewModel) {
    val achievements by viewModel.achievements.collectAsState()
    val shoppingList by viewModel.shoppingList.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Personal Kitchen", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text("Shopping List", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (shoppingList.isEmpty()) {
            item {
                Text("Your shopping list is empty.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            items(shoppingList) { item ->
                ListItem(
                    headlineContent = { Text(item) },
                    trailingContent = {
                        IconButton(onClick = { viewModel.removeFromShoppingList(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Remove")
                        }
                    },
                    leadingContent = { Icon(Icons.Default.ShoppingCart, contentDescription = null) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text("My Achievements", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(achievements) { achievement ->
            AchievementItem(achievement.title, achievement.description, achievement.isUnlocked)
        }
    }
}
