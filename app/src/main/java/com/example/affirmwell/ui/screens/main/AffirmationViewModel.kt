package com.example.affirmwell.ui.screens.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AffirmationViewModel : ViewModel() {
    private val _affirmations = MutableStateFlow(listOf("You are strong", "You are capable", "You are loved"))
    val affirmations: StateFlow<List<String>> get() = _affirmations

    private val _selectedCategory = MutableStateFlow("General")
    val selectedCategory: StateFlow<String> get() = _selectedCategory

    fun changeCategory() {
        // Logic to change the category and update affirmations
        _selectedCategory.value = "New Category"
        _affirmations.value = listOf("New affirmation 1", "New affirmation 2")
    }
}
