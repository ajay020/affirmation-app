package com.example.affirmwell.ui.screens.language

import Language
import LanguageViewModel
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.ui.theme.AffirmWellTheme
import com.example.affirmwell.utils.LocaleHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(
    onNavigateBack: () -> Unit,
    viewModel: LanguageViewModel = viewModel()
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val context = LocalContext.current
    LocaleHelper.applyLocale(context, selectedLanguage.code)

    LaunchedEffect(key1 = Unit) {
        viewModel.loadSelectedLanguage(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Language") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.clickable { onNavigateBack() }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            viewModel.languages.forEach { language ->
                LanguageItem(
                    language = language,
                    isSelected = language == selectedLanguage,
                    onClick = {
                        viewModel.selectLanguage(language, context)
                    }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(language: Language, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = language.name,
            modifier = Modifier.weight(1f),
            color = if (isSelected) Color.Blue else MaterialTheme.colorScheme.onBackground
        )
        if (isSelected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Selected",
                tint = Color.Blue
            )
        }
    }
}

@Preview
@Composable
private fun LanguageScreenPreview() {
    AffirmWellTheme(
        darkTheme = false
    ) {
        LanguageScreen(onNavigateBack = { /*TODO*/ })
    }
}
