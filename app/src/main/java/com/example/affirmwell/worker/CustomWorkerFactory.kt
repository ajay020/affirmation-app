package com.example.affirmwell.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.affirmwell.data.UserPreferencesRepository

class CustomWorkerFactory(
    private val userPreferencesRepository: UserPreferencesRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            NotificationWorker::class.java.name -> NotificationWorker(
                appContext, workerParameters, userPreferencesRepository
            )

            else -> null
        }
    }
}
