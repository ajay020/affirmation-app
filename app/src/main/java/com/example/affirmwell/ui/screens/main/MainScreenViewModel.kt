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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainScreenViewModel(
    val repository: AffirmationRepository,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val affirmations = MutableStateFlow<List<Affirmation>>(emptyList())

    private val _backgroundImageRes = MutableStateFlow(R.drawable.img1)
    var backgroundImageRes: StateFlow<Int> = _backgroundImageRes

    private val _selectedCategory = MutableStateFlow(Utils.catagories.first())
    val selectedCategory: StateFlow<Category?> get() = _selectedCategory


    fun loadAffirmationsByCategory(categoryName: String) {
        viewModelScope.launch {
            repository.getAffirmationsByCategory(categoryName).stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).collect {
                affirmations.value = it
            }
        }
    }

    init {
        selectedCategory.value?.let { loadAffirmationsByCategory(it.name) }
        viewModelScope.launch {
            userPreferencesRepository.backgroundImageRes.collect { backgroundImageRes ->
                _backgroundImageRes.value = backgroundImageRes
            }
        }
    }

    fun toggleFavorite(affirmation: Affirmation) {
        viewModelScope.launch {
            val updatedAffirmation = affirmation.copy(isFavorite = !affirmation.isFavorite)
            repository.updateAffirmation(updatedAffirmation)
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
        }
    }

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
