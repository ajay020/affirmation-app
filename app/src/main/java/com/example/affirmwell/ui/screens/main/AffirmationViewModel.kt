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
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.data.AffirmationRepository
import kotlinx.coroutines.launch

class AffirmationViewModel(val repository: AffirmationRepository) : ViewModel() {
    private val _affirmations = MutableStateFlow<List<Affirmation>>(emptyList())
    val affirmations: StateFlow<List<Affirmation>> get() = _affirmations

    private val _favoriteAffirmations = MutableStateFlow<List<Affirmation>>(emptyList())
    val favoriteAffirmations: StateFlow<List<Affirmation>> get() = _favoriteAffirmations

    private fun loadAffirmationsByCategory(category: String) {
        viewModelScope.launch {
            _affirmations.value = repository.getAffirmationsByCategory(category)
            Log.d("AffirmationViewModel", "Affirmations loaded: $affirmations.value")
        }
    }

    init {
        loadAffirmationsByCategory("Anxiety")
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

    fun loadFavoriteAffirmations() {
        viewModelScope.launch {
            _favoriteAffirmations.value = repository.getAffirmations()
        }
    }

    companion object {
        var Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY]) as AffirmationApplication
                AffirmationViewModel(application.container.affirmationRepository)
            }
        }
    }
}
