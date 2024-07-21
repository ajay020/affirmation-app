package com.example.affirmwell.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.affirmwell.ui.screens.addAffirmation.AddAffirmationScreen
import com.example.affirmwell.ui.screens.language.LanguageScreen
import com.example.affirmwell.ui.screens.main.MainScreen
import com.example.affirmwell.ui.screens.notificationSetting.NotificationSettingsScreen
import com.example.affirmwell.ui.screens.settings.SettingsScreen

object Screens {
    const val MAIN = "main"
    const val SETTINGS = "settings"
    const val LANGUAGE = "language"
    const val PRIVACY_POLICY = "privacy_policy"
    const val NOTIFICATIONS = "notifications"
    const val ADD_PERSONAL_AFFIRMATION = "add_personal_affirmation"
    // Add other screens here
}

@Composable
fun AffirmationNavigation(modifier: Modifier = Modifier) {

    var navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screens.MAIN) {
        composable(Screens.MAIN) {
            MainScreen(onNavigateToSettings = { navController.navigate(Screens.SETTINGS) })
        }
        composable(Screens.SETTINGS) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSetNotificationTime = { navController.navigate(Screens.NOTIFICATIONS) },
                onAddPersonalAffirmation = { navController.navigate(Screens.ADD_PERSONAL_AFFIRMATION) },
                onLanguage = { navController.navigate(Screens.LANGUAGE) },
                onPrivacyPolicy = { navController.navigate(Screens.PRIVACY_POLICY) }
            )
        }

        composable(Screens.NOTIFICATIONS) {
            NotificationSettingsScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screens.ADD_PERSONAL_AFFIRMATION) {
            AddAffirmationScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screens.LANGUAGE) {
            LanguageScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screens.PRIVACY_POLICY) {
//            PrivacyPolicyScreen(onNavigateBack = { navController.popBackStack() })
        }

    }
}