package com.example.affirmwell.ui.screens.notificationSetting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.affirmwell.AffirmationApplication
import com.example.affirmwell.data.UserPreferencesRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationSettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _numberOfNotifications = MutableStateFlow(1)
    val numberOfNotifications: StateFlow<Int> get() = _numberOfNotifications

    private val _startTime = MutableStateFlow("08:00")
    val startTime: StateFlow<String> get() = _startTime

    private val _endTime = MutableStateFlow("20:00")
    val endTime: StateFlow<String> get() = _endTime

    private val _selectedDays = MutableStateFlow<Set<String>>(setOf())
    val selectedDays: StateFlow<Set<String>> get() = _selectedDays

    init {
        viewModelScope.launch {
            userPreferencesRepository.numberOfNotifications.collect {
                _numberOfNotifications.value = it
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.startTime.collect { _startTime.value = it }
        }
        viewModelScope.launch {
            userPreferencesRepository.endTime.collect { _endTime.value = it }
        }
        viewModelScope.launch {
            userPreferencesRepository.selectedDays.collect { _selectedDays.value = it }
        }
    }

    fun setNumberOfNotifications(number: Int) {
        _numberOfNotifications.value = number
    }

    fun setStartTime(time: String) {
        _startTime.value = time
    }

    fun setEndTime(time: String) {
        _endTime.value = time
    }

    fun toggleDay(day: String) {
        val currentDays = _selectedDays.value.toMutableSet()
        if (currentDays.contains(day)) {
            currentDays.remove(day)
        } else {
            currentDays.add(day)
        }
        _selectedDays.value = currentDays
    }

    fun saveSettings() {
        viewModelScope.launch {
            try {
                val saveNumberOfNotifications = async {
                    userPreferencesRepository.saveNumberOfNotifications(_numberOfNotifications.value)
                }
                val saveStartTime = async {
                    userPreferencesRepository.saveStartTime(_startTime.value)
                }
                val saveEndTime = async {
                    userPreferencesRepository.saveEndTime(_endTime.value)
                }
                val saveSelectedDays = async {
                    userPreferencesRepository.saveSelectedDays(_selectedDays.value)
                }

                // Await all save operations
                saveNumberOfNotifications.await()
                saveStartTime.await()
                saveEndTime.await()
                saveSelectedDays.await()

                Log.d("SaveSettings", "Settings saved successfully")
            } catch (e: Exception) {
                Log.e("SaveSettings", "Error saving settings: ${e.message}")
            }
        }
    }


    // Inject dependency into view model
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY]) as AffirmationApplication
                NotificationSettingsViewModel(application.userPreferencesRepository)
            }
        }
    }
}

data class NotificationSettings(
    val numberOfNotifications: Int,
    val startTime: String,
    val endTime: String,
    val selectedDays: Set<String>
)
