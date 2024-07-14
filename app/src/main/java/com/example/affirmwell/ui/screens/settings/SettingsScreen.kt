package com.example.affirmwell.ui.screens.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onSetNotificationTime: () -> Unit,
    onAddPersonalAffirmation: () -> Unit,
    onAboutApp: () -> Unit,
    onLanguage: () -> Unit,
    onPrivacyPolicy: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SettingsItem(
                title = "Notifications",
                onClick = onSetNotificationTime
            )
            SettingsItem(
                title = "Add Personal Affirmation",
                onClick = onAddPersonalAffirmation
            )
            SettingsItem(
                title = "Language",
                onClick = onLanguage
            )
            SettingsItem(
                title = "About the App",
                onClick = onAboutApp
            )
            SettingsItem(
                title = "Privacy Policy",
                onClick = onPrivacyPolicy
            )
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        headlineContent = { Text(title) }
    )
    HorizontalDivider()
}

@Preview(showBackground = true)
@Composable
private fun SettingsItemPreview() {
//    SettingsItem(
//        title = "Set Notification Time",
//        onClick = {}
//    )

    SettingsScreen(
        onNavigateBack = { /*TODO*/ },
        onSetNotificationTime = { /*TODO*/ },
        onAddPersonalAffirmation = { /*TODO*/ },
        onAboutApp = { /*TODO*/ },
        onLanguage = { /*TODO*/ }) {

    }
}