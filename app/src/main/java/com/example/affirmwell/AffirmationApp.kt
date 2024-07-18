package com.example.affirmwell

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.affirmwell.ui.screens.addAffirmation.AddAffirmationScreen
import com.example.affirmwell.ui.screens.main.MainScreen
import com.example.affirmwell.ui.screens.notificationSetting.NotificationSettingsScreen
import com.example.affirmwell.ui.screens.settings.SettingsScreen

sealed class Screens(val route: String) {
    object MainScreen : Screens("main")
    object SettingsScreen : Screens("settings")
    object NotificationSettingsScreen : Screens("notification_settings")
    object AddAffirmationScreen : Screens("add_affirmation")
}

@Composable
fun AffirmationApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.MainScreen.route
    ) {
        composable(route = Screens.MainScreen.route) {
            MainScreen(
                onNavigateToSettings = { navController.navigate(Screens.SettingsScreen.route) }
            )
        }
        composable(route = Screens.SettingsScreen.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSetNotificationTime = { navController.navigate(Screens.NotificationSettingsScreen.route) },
                onAddPersonalAffirmation = { navController.navigate(Screens.AddAffirmationScreen.route) },
                onAboutApp = { /*TODO*/ },
                onLanguage = { /*TODO*/ }) {
            }
        }
        composable(Screens.NotificationSettingsScreen.route) {
            NotificationSettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screens.AddAffirmationScreen.route) {
            AddAffirmationScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

    }
}