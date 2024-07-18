package com.example.affirmwell.data

import android.content.Context
import kotlinx.coroutines.flow.Flow


interface AffirmationRepository {
    fun getAffirmationsByCategory(category: String): Flow<List<Affirmation>>
    suspend fun addCustomAffirmation(affirmation: Affirmation)
    suspend fun initializeDefaultAffirmations(context: Context)
    suspend fun updateAffirmation(affirmation: Affirmation)
    suspend fun deleteAffirmation(affirmation: Affirmation)
    suspend fun getAffirmations(): List<Affirmation>
    suspend fun isDatabaseEmpty(): Boolean
}
