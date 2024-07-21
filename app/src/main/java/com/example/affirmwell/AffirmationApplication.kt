package com.example.affirmwell

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.affirmwell.data.AppContainer
import com.example.affirmwell.data.AppDataContainer
import com.example.affirmwell.data.UserPreferencesRepository
import com.example.affirmwell.utils.LocaleHelper
import com.example.affirmwell.worker.AffirmationNotificationManager
import com.example.affirmwell.worker.CustomWorkerFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.Locale


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
    lateinit var affirmationNotificationManager: AffirmationNotificationManager

    override fun onCreate() {
        super.onCreate()

        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore = dataStore)

        // Initialize default affirmations
        applicationScope.launch(Dispatchers.IO) {
            container.affirmationRepository.initializeDefaultAffirmations(this@AffirmationApplication)
        }

        // Inject user preference repository instance into notification worker
        val customWorkerFactory = CustomWorkerFactory(userPreferencesRepository)
        val config = Configuration.Builder().setWorkerFactory(customWorkerFactory).build()
        WorkManager.initialize(this, config)

        affirmationNotificationManager =
            AffirmationNotificationManager(this, userPreferencesRepository)
        affirmationNotificationManager.scheduleNotifications()

        // Set the default locale to English
        LocaleHelper.setLocale(this)
    }
}