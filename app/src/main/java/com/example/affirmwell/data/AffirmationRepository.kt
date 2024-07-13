package com.example.affirmwell.data

import android.content.Context


interface AffirmationRepository {
    suspend fun getAffirmationsByCategory(category: String): List<Affirmation>
    suspend fun addCustomAffirmation(affirmation: Affirmation)
    suspend fun initializeDefaultAffirmations(context: Context)
    suspend fun updateAffirmation(affirmation: Affirmation)
    suspend fun getAffirmations(): List<Affirmation>
}
