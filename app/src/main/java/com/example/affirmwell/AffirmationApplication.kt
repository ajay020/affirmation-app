package com.example.affirmwell

import android.app.Application
import com.example.affirmwell.data.AppContainer
import com.example.affirmwell.data.AppDataContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AffirmationApplication : Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        // Initialize default affirmations
        applicationScope.launch(Dispatchers.IO) {
            container.affirmationRepository.initializeDefaultAffirmations(this@AffirmationApplication)
        }
    }
}