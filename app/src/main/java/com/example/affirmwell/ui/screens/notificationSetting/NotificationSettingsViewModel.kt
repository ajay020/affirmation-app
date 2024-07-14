package com.example.affirmwell.ui.screens.notificationSetting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationSettingsViewModel : ViewModel() {
    private val _numberOfNotifications = MutableStateFlow(1)
    val numberOfNotifications: StateFlow<Int> get() = _numberOfNotifications

    private val _startTime = MutableStateFlow("08:00")
    val startTime: StateFlow<String> get() = _startTime

    private val _endTime = MutableStateFlow("20:00")
    val endTime: StateFlow<String> get() = _endTime

    private val _selectedDays = MutableStateFlow<Set<String>>(setOf())
    val selectedDays: StateFlow<Set<String>> get() = _selectedDays

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
        // Save the settings to the database or perform any other necessary actions
        val settings = NotificationSettings(
            numberOfNotifications = _numberOfNotifications.value,
            startTime = _startTime.value,
            endTime = _endTime.value,
            selectedDays = _selectedDays.value
        )
    }
}

data class NotificationSettings(
    val numberOfNotifications: Int,
    val startTime: String,
    val endTime: String,
    val selectedDays: Set<String>
)
