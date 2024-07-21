package com.example.affirmwell.utils

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    private const val LANGUAGE_PREFERENCES = "language_prefs"
    private const val LANGUAGE_KEY = "selected_language"

    fun setLocale(context: Context): Context {
        val preferences = context.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE)
        val selectedLanguageCode = preferences.getString(LANGUAGE_KEY, "en") ?: "en" // Default to English
        return updateResources(context, selectedLanguageCode)
    }

    fun getSelectedLanguage(context: Context): String {
        val preferences = context.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE)
        return preferences.getString(LANGUAGE_KEY, "en") ?: "en" // Default to English
    }

    fun setNewLocale(context: Context, languageCode: String) {
        persistLanguage(context, languageCode)
        updateResources(context, languageCode)
    }

    private fun persistLanguage(context: Context, languageCode: String) {
        val preferences = context.getSharedPreferences(LANGUAGE_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit().putString(LANGUAGE_KEY, languageCode).apply()
    }

    private fun updateResources(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    fun applyLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }
}
