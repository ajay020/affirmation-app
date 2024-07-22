package com.example.affirmwell.ui.screens.addAffirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.affirmwell.AffirmationApplication
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.data.AffirmationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AddAffirmationViewModel(
    private val repository: AffirmationRepository
) : ViewModel() {
    val affirmations = repository.getAffirmationsByCategory("My Affirmations").stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addAffirmation(affirmation: Affirmation) {
        viewModelScope.launch {
            repository.addCustomAffirmation(affirmation)
        }
    }

    fun updateAffirmation(affirmation: Affirmation) {
        viewModelScope.launch {
            repository.updateAffirmation(affirmation)
        }
    }

    fun deleteAffirmation(affirmation: Affirmation) {
        viewModelScope.launch {
            repository.deleteAffirmation(affirmation)
        }
    }

    companion object {
        val FACTORY: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AffirmationApplication)
                val repository = application.container.affirmationRepository
                AddAffirmationViewModel(repository)
            }
        }
    }
}