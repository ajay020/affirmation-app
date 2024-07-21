package com.example.affirmwell.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PrivacyTip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.affirmwell.R

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.padding(end = 16.dp)
        )
        Text(text = title, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onSetNotificationTime: () -> Unit,
    onAddPersonalAffirmation: () -> Unit,
    onLanguage: () -> Unit,
    onPrivacyPolicy: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.settings)) },
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
                icon = Icons.Outlined.Notifications,
                title = stringResource(id = R.string.notifications),
                onClick = onSetNotificationTime
            )
            SettingsItem(
                icon = Icons.Outlined.Person,
                title = stringResource(id = R.string.add_personal_affirmation),
                onClick = onAddPersonalAffirmation
            )
            SettingsItem(
                icon = Icons.Outlined.Language,
                title = stringResource(id = R.string.language),
                onClick = onLanguage
            )
            SettingsItem(
                icon = Icons.Outlined.PrivacyTip,
                title = stringResource(id = R.string.privacy_policy),
                onClick = onPrivacyPolicy
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsItemPreview() {
    SettingsScreen(
        onNavigateBack = { /*TODO*/ },
        onSetNotificationTime = { /*TODO*/ },
        onAddPersonalAffirmation = { /*TODO*/ },
        onLanguage = { /*TODO*/ }) {
    }
}