package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ExploreScreen(
    viewModel: MainViewModel,
    onCountryClick: (String) -> Unit
) {
    val countries by viewModel.allCountries.collectAsState()
    val continents = listOf("All", "Asia", "Europe", "Africa", "Americas", "Oceania")
    var selectedContinent by remember { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Interactive Map", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Explore World Food Academy through regions", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        ScrollableTabRow(
            selectedTabIndex = continents.indexOf(selectedContinent).coerceAtLeast(0),
            edgePadding = 0.dp,
            divider = {}
        ) {
            continents.forEach { continent ->
                Tab(
                    selected = selectedContinent == continent,
                    onClick = { selectedContinent = continent },
                    text = { Text(continent) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        val filteredCountries = if (selectedContinent == "All") countries 
                               else countries.filter { it.continent == selectedContinent }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredCountries) { country ->
                CountryCard(country.nameEn, country.continent) { onCountryClick(country.id) }
            }
        }
    }
}

@Composable
fun CountryCard(name: String, continent: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.Center) {
            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(continent, style = MaterialTheme.typography.bodySmall)
        }
    }
}
