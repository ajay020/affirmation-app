package com.example.affirmwell

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.affirmwell.data.AppContainer
import com.example.affirmwell.data.AppDataContainer
import com.example.affirmwell.data.UserPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


private const val USER_PREFERENCE_NAME = "user_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCE_NAME
)

class AffirmationApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    lateinit var container: AppContainer
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore = dataStore)

        // Initialize default affirmations
        applicationScope.launch(Dispatchers.IO) {
            container.affirmationRepository.initializeDefaultAffirmations(this@AffirmationApplication)
        }
    }
}