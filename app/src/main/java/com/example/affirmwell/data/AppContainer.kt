package com.example.affirmwell.data

import android.content.Context

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val affirmationRepository: AffirmationRepository
}

/**
 * [AppContainer] implementation that provides instance of [AffirmationRepositoryImpl]
 */
class AppDataContainer(private val context: Context) : AppContainer {

    override val affirmationRepository: AffirmationRepository by lazy {
        AffirmationRepositoryImpl(AffirmationDatabase.getDatabase(context).affirmationDao())
    }
}