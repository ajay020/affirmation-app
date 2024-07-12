package com.example.affirmwell

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.affirmwell.ui.screens.main.AffirmationViewModel
import com.example.affirmwell.ui.screens.main.MainScreen

@Composable
fun AffirmationApp(modifier: Modifier = Modifier) {
    val viewModel = AffirmationViewModel()
    Scaffold {
        MainScreen(
            viewModel = viewModel,
            modifier = modifier.padding(it)
        )
    }
}