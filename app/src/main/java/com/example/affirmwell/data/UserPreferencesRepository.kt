package com.example.affirmwell.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.example.affirmwell.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/*
 * Concrete class implementation to access data store
 */
class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val BACKGROUND_IMAGE_RES = intPreferencesKey("background_image_res")
        val NUMBER_OF_NOTIFICATIONS = intPreferencesKey("number_of_notifications")
        val START_TIME = stringPreferencesKey("start_time")
        val END_TIME = stringPreferencesKey("end_time")
        val SELECTED_DAYS = stringSetPreferencesKey("selected_days")

        const val TAG = "UserPreferencesRepo"
    }

    val backgroundImageRes: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[BACKGROUND_IMAGE_RES] ?: R.drawable.img1 // Default value if not found
        }

    suspend fun saveBackgroundImageResPreference(backgroundImageRes: Int) {
        dataStore.edit { preferences ->
            preferences[BACKGROUND_IMAGE_RES] = backgroundImageRes
        }
    }

    // Methods to save notification settings
    suspend fun saveNumberOfNotifications(numberOfNotifications: Int) {
        dataStore.edit { preferences ->
            preferences[NUMBER_OF_NOTIFICATIONS] = numberOfNotifications
        }
    }

    suspend fun saveStartTime(startTime: String) {
        dataStore.edit { preferences ->
            preferences[START_TIME] = startTime
        }
    }

    suspend fun saveEndTime(endTime: String) {
        dataStore.edit { preferences ->
            preferences[END_TIME] = endTime
        }
    }

    suspend fun saveSelectedDays(selectedDays: Set<String>) {
        Log.d(TAG, "Saving Selected Days: $selectedDays")
        dataStore.edit { preferences ->
            preferences[SELECTED_DAYS] = selectedDays
        }
    }

    // Read notification settings
    val numberOfNotifications: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[NUMBER_OF_NOTIFICATIONS] ?: 1
        }

    val startTime: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[START_TIME] ?: "08:00"
        }

    val endTime: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[END_TIME] ?: "20:00"
        }

    val selectedDays: Flow<Set<String>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences.", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedDays = preferences[SELECTED_DAYS] ?: emptySet()
            Log.d(TAG, "Loaded Selected Days: $selectedDays")
            selectedDays
        }
}