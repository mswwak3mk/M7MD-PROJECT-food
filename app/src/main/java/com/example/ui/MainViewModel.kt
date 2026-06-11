package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiClient
import com.example.data.entity.*
import com.example.data.repository.FoodRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class MainViewModel(private val repository: FoodRepository) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("Hello! I am the World Food Academy Chef. How can I help you today?", false)
    ))
    val chatMessages = _chatMessages.asStateFlow()

    val allRecipes = repository.allRecipes.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allCountries = repository.allCountries.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val allCities = repository.allCities.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
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

    private val _citySearchQuery = MutableStateFlow("")
    val citySearchQuery = _citySearchQuery.asStateFlow()

    private val _shoppingList = MutableStateFlow<List<String>>(emptyList())
    val shoppingList = _shoppingList.asStateFlow()

    val cityResults = citySearchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(emptyList())
            else repository.searchCities(query)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateCitySearchQuery(query: String) {
        _citySearchQuery.value = query
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            repository.toggleFavorite(recipe)
        }
    }

    fun addToShoppingList(item: String) {
        if (!_shoppingList.value.contains(item)) {
            _shoppingList.value += item
        }
    }

    fun removeFromShoppingList(item: String) {
        _shoppingList.value -= item
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

    private var isDataInitialized = false

    fun initMockData() {
        if (isDataInitialized) return
        isDataInitialized = true
        
        viewModelScope.launch {
            val currentCountries = repository.allCountries.first()
            if (currentCountries.isEmpty()) {
                val jordan = Country("jo", "Jordan", "الأردن", "Asia", "", "The land of Petra and hospitality.")
                val italy = Country("it", "Italy", "إيطاليا", "Europe", "", "Home of pasta and art.")
                val japan = Country("jp", "Japan", "اليابان", "Asia", "", "Land of the rising sun and sushi.")
                val saudi = Country("sa", "Saudi Arabia", "المملكة العربية السعودية", "Asia", "", "The heart of the Arabian Peninsula.")
                
                repository.insertCountry(jordan)
                repository.insertCountry(italy)
                repository.insertCountry(japan)
                repository.insertCountry(saudi)

                // Mock Cities
                repository.insertCity(City("amman", "Amman", "عمان", "jo", "The capital of Jordan, known for street food."))
                repository.insertCity(City("riyadh", "Riyadh", "الرياض", "sa", "The capital of Saudi Arabia, home to Najdi flavors."))
                repository.insertCity(City("rome", "Rome", "روما", "it", "The Eternal City and birthplace of Carbonara."))
                repository.insertCity(City("tokyo", "Tokyo", "طوكيو", "jp", "The world's most Michelin-starred city."))

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

                val kabsa = Recipe(
                    id = "kabsa",
                    nameEn = "Kabsa",
                    nameAr = "كبسة",
                    nameLocal = "الكبسة السعودية",
                    countryId = "sa",
                    category = "Lunch",
                    cuisineType = "Traditional",
                    history = "The most popular dish in Saudi Arabia, a fragrant rice dish made with meat and spices.",
                    imageUrl = "https://images.unsplash.com/photo-1633945274405-b6c8069047b0?q=80&w=1000",
                    preparationTime = 20,
                    cookingTime = 90,
                    difficulty = "Medium",
                    calories = 700,
                    servings = 4,
                    nutritionFacts = "Carbohydrates, Protein",
                    allergens = "None",
                    chefTips = "Searing the meat first adds incredible depth to the rice."
                )

                repository.insertRecipe(mansaf, 
                    listOf(Ingredient(recipeId = "mansaf", name = "Lamb", amount = "2", unit = "kg")),
                    listOf(Instruction(recipeId = "mansaf", stepNumber = 1, description = "Cook the lamb with spices until tender."))
                )

                repository.insertRecipe(kabsa,
                    listOf(Ingredient(recipeId = "kabsa", name = "Chicken or Lamb", amount = "1", unit = "kg")),
                    listOf(Instruction(recipeId = "kabsa", stepNumber = 1, description = "Sauté onions and spices, then add meat and water."))
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
