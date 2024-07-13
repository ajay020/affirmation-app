package com.example.affirmwell.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AffirmationRepositoryImpl(private val affirmationDao: AffirmationDao  ) : AffirmationRepository {

    override suspend fun initializeDefaultAffirmations(context: Context) {
        withContext(Dispatchers.IO) {
            val inputStream = context.assets.open("default_affirmations.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val affirmations = parseAffirmations(json)
            affirmationDao.insertAffirmations(affirmations)
        }
    }

    override suspend fun updateAffirmation(affirmation: Affirmation) {
        affirmationDao.updateAffirmation(affirmation)
    }

    override suspend fun getAffirmations(): List<Affirmation> {
        return affirmationDao.getFavoriteAffirmations()
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

    override suspend fun getAffirmationsByCategory(category: String): List<Affirmation> {
        return affirmationDao.getAffirmationsByCategory(category)
    }

    override suspend fun addCustomAffirmation(affirmation: Affirmation) {
        affirmationDao.insertAffirmation(affirmation)
    }
}
