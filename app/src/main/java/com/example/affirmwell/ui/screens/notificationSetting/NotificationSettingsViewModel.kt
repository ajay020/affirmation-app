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
import com.example.affirmwell.utils.Utils.sliderValueToTime
import com.example.affirmwell.worker.AffirmationNotificationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationSettingsViewModel(
    val userPreferencesRepository: UserPreferencesRepository,
    val affirmationNotificationManager: AffirmationNotificationManager
) : ViewModel() {
    private val _numberOfNotifications = MutableStateFlow(1)
    val numberOfNotifications: StateFlow<Int> get() = _numberOfNotifications

    private val _startTime = MutableStateFlow("8:00")
    val startTime: StateFlow<String> get() = _startTime

    private val _endTime = MutableStateFlow("20:00")
    val endTime: StateFlow<String> get() = _endTime

    private val _selectedDays = MutableStateFlow<Set<String>>(setOf())
    val selectedDays: StateFlow<Set<String>> get() = _selectedDays

    private val _notificationsEnabled = MutableStateFlow(false)
    val notificationsEnabled: StateFlow<Boolean> get() = _notificationsEnabled

    private val _sliderPosition = MutableStateFlow(0f to 12f)  // Default slider positions
    val sliderPosition: StateFlow<Pair<Float, Float>> get() = _sliderPosition

    private var sliderUpdateJob: Job? = null


    init {
        viewModelScope.launch {
            userPreferencesRepository.numberOfNotifications.collect {
                _numberOfNotifications.value = it
            }
        }
//        viewModelScope.launch {
//            userPreferencesRepository.startTime.collect {
//                _startTime.value = it
//            }
//        }
//        viewModelScope.launch {
//            userPreferencesRepository.endTime.collect { _endTime.value = it }
//        }
        viewModelScope.launch {
            userPreferencesRepository.selectedDays.collect { _selectedDays.value = it }
        }

        viewModelScope.launch {
            userPreferencesRepository.notificationsEnabled.collect {
                _notificationsEnabled.value = it
            }
        }
        viewModelScope.launch {
            userPreferencesRepository.sliderValues.collect { sliderValues ->
                _sliderPosition.value = sliderValues
                _startTime.value = sliderValueToTime(sliderValues.first)
                _endTime.value = sliderValueToTime(sliderValues.second)
            }
        }
    }

    private fun saveTimeSettings() {
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveStartTime(_startTime.value)
                userPreferencesRepository.saveEndTime(_endTime.value)
            } catch (e: Exception) {
                Log.e("SaveTimeSettings", "Error saving time settings: ${e.message}")
            }
        }
    }

    fun setNumberOfNotifications(number: Int) {
        _numberOfNotifications.value = number
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveNumberOfNotifications(number)
            } catch (e: Exception) {
                Log.e(
                    "SaveNumberOfNotifications",
                    "Error saving number of notifications: ${e.message}"
                )
            }
        }
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
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveSelectedDays(_selectedDays.value)
            } catch (e: Exception) {
                Log.e("SaveSelectedDays", "Error saving selected days: ${e.message}")
            }
        }
    }

    fun toggleNotificationsEnabled() {
        _notificationsEnabled.value = !_notificationsEnabled.value
        if (_notificationsEnabled.value) {
            affirmationNotificationManager.scheduleNotifications()
        } else {
            affirmationNotificationManager.cancelNotifications()
        }
        viewModelScope.launch {
            try {
                userPreferencesRepository.saveNotificationsEnabled(_notificationsEnabled.value)
            } catch (e: Exception) {
                Log.e(
                    "SaveNotificationsEnabled",
                    "Error saving notifications enabled: ${e.message}"
                )
            }
        }
    }

    fun onSliderValueChange(start: Float, end: Float) {
        if (end - start >= 1f) {
            _sliderPosition.value = start to end
            _startTime.value = sliderValueToTime(start)
            _endTime.value = sliderValueToTime(end)
            debounceSliderUpdate(start, end)
        } else {
            // Adjust the end time to maintain a 1-hour difference
            val adjustedEnd = start + 1f
            _sliderPosition.value = start to adjustedEnd
            _startTime.value = sliderValueToTime(start)
            _endTime.value = sliderValueToTime(end)
            debounceSliderUpdate(start, adjustedEnd)
        }
    }

    private fun debounceSliderUpdate(start: Float, end: Float) {
        sliderUpdateJob?.cancel()
        sliderUpdateJob = viewModelScope.launch {
            delay(300)  // Adjust the delay as needed
            userPreferencesRepository.saveSliderValues(start, end)
//            launch {
//                saveTimeSettings()
//            }
        }
    }



    // Inject dependency into view model
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY]) as AffirmationApplication
                NotificationSettingsViewModel(
                    userPreferencesRepository = application.userPreferencesRepository,
                    affirmationNotificationManager = application.affirmationNotificationManager
                )
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
