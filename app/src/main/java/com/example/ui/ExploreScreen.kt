package com.example.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExploreScreen(
    viewModel: MainViewModel,
    onCountryClick: (String) -> Unit,
    onNavigateToChef: () -> Unit
) {
    val countries by viewModel.allCountries.collectAsState()
    val citySearchQuery by viewModel.citySearchQuery.collectAsState()
    val cityResults by viewModel.cityResults.collectAsState()
    
    val continents = listOf("All", "Asia", "Europe", "Africa", "Americas", "Oceania")
    var selectedContinent by remember { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("World Explorer", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Search any city or browse by continent", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = citySearchQuery,
            onValueChange = { viewModel.updateCitySearchQuery(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search city (e.g. Riyadh, Tokyo, London)") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            shape = RoundedCornerShape(12.dp)
        )

        if (citySearchQuery.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Search Results", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                if (cityResults.isEmpty()) {
                    Text("No local results", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(cityResults) { city ->
                    CountryCard(city.nameEn, "City in ${city.countryId.uppercase()}") {
                        viewModel.askChef("Tell me about the best food and culture in ${city.nameEn}, ${city.countryId}")
                        onNavigateToChef()
                    }
                }
                
                // AI "Search and Learn" prompt always available when searching
                item(span = { GridItemSpan(2) }) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clickable {
                                viewModel.askChef("What are the most famous dishes in $citySearchQuery? Please describe their flavors and where they come from.")
                                onNavigateToChef()
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Ask the Academy about $citySearchQuery", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text("Get AI insights for this city", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))
            ScrollableTabRow(
                selectedTabIndex = continents.indexOf(selectedContinent).coerceAtLeast(0),
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent
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

            if (filteredCountries.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No countries added for this region yet.", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredCountries) { country ->
                        CountryCard(country.nameEn, country.continent) { onCountryClick(country.id) }
                    }
                }
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
