package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiClient
import com.example.data.entity.*
import com.example.data.repository.FoodRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

class MainViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("Hello! I am the World Food Academy Chef. How can I help you today?", false)
    ))
    val chatMessages = _chatMessages.asStateFlow()

    val allRecipes = repository.allRecipes.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allCountries = repository.allCountries.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val favoriteRecipes = repository.favoriteRecipes.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val achievements = repository.allAchievements.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val searchResults = searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else repository.searchRecipes(query)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe)
        }
    }

    // AI Components
    private val _isChefLoading = MutableStateFlow(false)
    val isChefLoading = _isChefLoading.asStateFlow()

    fun askChef(question: String) {
        if (question.isBlank()) return
        
        viewModelScope.launch {
            _chatMessages.value += ChatMessage(question, true)
            _isChefLoading.value = true
            
            val response = GeminiClient.getChefResponse(question)
            
            _chatMessages.value += ChatMessage(response, false)
            _isChefLoading.value = false
        }
    }

    // Initialization with Mock Data
    fun initMockData() {
        viewModelScope.launch {
            if (allCountries.value.isEmpty()) {
                val jordan = Country("jo", "Jordan", "الأردن", "Asia", "", "The land of Petra and hospitality.")
                val italy = Country("it", "Italy", "إيطاليا", "Europe", "", "Home of pasta and art.")
                val japan = Country("jp", "Japan", "اليابان", "Asia", "", "Land of the rising sun and sushi.")
                
                repository.insertCountry(jordan)
                repository.insertCountry(italy)
                repository.insertCountry(japan)

                val mansaf = Recipe(
                    id = "mansaf",
                    nameEn = "Mansaf",
                    nameAr = "منسف",
                    nameLocal = "المنسف الأردني",
                    countryId = "jo",
                    category = "Lunch",
                    cuisineType = "Traditional",
                    history = "The national dish of Jordan, a symbol of generosity and hospitality.",
                    imageUrl = "https://images.unsplash.com/photo-1541518763669-279f00ed4342?q=80&w=1000",
                    preparationTime = 30,
                    cookingTime = 120,
                    difficulty = "Hard",
                    calories = 850,
                    servings = 6,
                    nutritionFacts = "High protein, High fat",
                    allergens = "Milk (Jameed), Gluten (Shrak bread)",
                    chefTips = "Use high quality sheep Jameed for the best taste."
                )

                repository.insertRecipe(mansaf, 
                    listOf(Ingredient(recipeId = "mansaf", name = "Lamb", amount = "2", unit = "kg")),
                    listOf(Instruction(recipeId = "mansaf", stepNumber = 1, description = "Cook the lamb with spices until tender."))
                )
            }
        }
    }
}

class MainViewModelFactory(private val repository: FoodRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
