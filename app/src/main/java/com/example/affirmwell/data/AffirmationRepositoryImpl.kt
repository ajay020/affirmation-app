package com.example.affirmwell.data

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import org.json.JSONObject

val TAG = "AffirmationRepository"

class AffirmationRepositoryImpl(private val affirmationDao: AffirmationDao) :
    AffirmationRepository {

    override suspend fun initializeDefaultAffirmations(context: Context) {

        if (isDatabaseEmpty()) {
            withContext(Dispatchers.IO) {
                val inputStream = context.assets.open("default_affirmations.json")
                val json = inputStream.bufferedReader().use { it.readText() }
                val affirmations = parseAffirmations(json)
                affirmationDao.insertAffirmations(affirmations)
            }
        }
    }

    override suspend fun updateAffirmation(affirmation: Affirmation) {
        affirmationDao.updateAffirmation(affirmation)
    }

    override suspend fun deleteAffirmation(affirmation: Affirmation) {
        affirmationDao.deleteAffirmation(affirmation)
    }


    override suspend fun getAffirmations(): List<Affirmation> {
        return affirmationDao.getFavoriteAffirmations()
    }

    override suspend fun isDatabaseEmpty(): Boolean {
        return affirmationDao.getAffirmationsCount() == 0
    }

    private fun parseAffirmations(json: String): List<Affirmation> {
        val jsonObject = JSONObject(json)
        val affirmations = mutableListOf<Affirmation>()
        val categories = jsonObject.keys()

        while (categories.hasNext()) {
            val category = categories.next()
            val affirmationsArray = jsonObject.getJSONArray(category)
            for (i in 0 until affirmationsArray.length()) {
                val affirmationText = affirmationsArray.getString(i)
                affirmations.add(Affirmation(category = category, text = affirmationText))
            }
        }
        return affirmations
    }

    override fun getAffirmationsByCategory(category: String): Flow<List<Affirmation>> {
        return affirmationDao.getAffirmationsByCategory(category)
    }

    override suspend fun addCustomAffirmation(affirmation: Affirmation) {
        affirmationDao.insertAffirmation(affirmation)
    }
}
