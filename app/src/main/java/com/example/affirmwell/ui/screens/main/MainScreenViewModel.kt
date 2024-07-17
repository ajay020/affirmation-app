package com.example.affirmwell.ui.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.affirmwell.AffirmationApplication
import com.example.affirmwell.R
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.data.AffirmationRepository
import com.example.affirmwell.data.UserPreferencesRepository
import com.example.affirmwell.model.Category
import com.example.affirmwell.utils.Utils
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainScreenViewModel(
    val repository: AffirmationRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _affirmations = MutableStateFlow<List<Affirmation>>(emptyList())
    val affirmations: StateFlow<List<Affirmation>> get() = _affirmations

    private val _backgroundImageRes = MutableStateFlow(R.drawable.img1)
    var backgroundImageRes: StateFlow<Int> = _backgroundImageRes

    private val _selectedCategory = MutableStateFlow<Category>(Utils.catagories.first())
    val selectedCategory: StateFlow<Category?> get() = _selectedCategory


    private fun loadAffirmationsByCategory(categoryName: String) {
        viewModelScope.launch {
            _affirmations.value = repository.getAffirmationsByCategory(categoryName)
            Log.d("AffirmationViewModel", "Affirmations loaded: $affirmations.value")
        }
    }

    init {
        selectedCategory.value?.let { loadAffirmationsByCategory(it.name) }
        viewModelScope.launch {
            val backgroundImageRes =
                userPreferencesRepository.backgroundImageRes.collect { backgroundImageRes ->
                    _backgroundImageRes.value = backgroundImageRes
                }
            Log.d("MainScreenViewModel", "BackgroundImageRes: $backgroundImageRes")
        }
    }

    fun addCustomAffirmation(text: String, category: String) {
        viewModelScope.launch {
            repository.addCustomAffirmation(
                Affirmation(
                    text = text,
                    category = category,
                    isCustom = true
                )
            )
            loadAffirmationsByCategory(category)
        }
    }

    fun toggleFavorite(affirmation: Affirmation) {
        viewModelScope.launch {
            val updatedAffirmation = affirmation.copy(isFavorite = !affirmation.isFavorite)
            repository.updateAffirmation(updatedAffirmation)
            loadAffirmationsByCategory(affirmation.category)
        }
    }

    fun saveBackgroundImageInDataStore(imageRes: Int) {
        viewModelScope.launch {
            userPreferencesRepository.saveBackgroundImageResPreference(imageRes)
        }
    }

    fun saveCategoryInDataStore(category: Category) {
        _selectedCategory.value = category
        viewModelScope.launch {
            userPreferencesRepository.saveCategoryPreference(category)
            loadAffirmationsByCategory(category.name)
        }
    }

//    fun loadFavoriteAffirmations() {
//        viewModelScope.launch {
//            _favoriteAffirmations.value = repository.getAffirmations()
//        }
//    }

    companion object {
        var Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY]) as AffirmationApplication
                MainScreenViewModel(
                    application.container.affirmationRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}
