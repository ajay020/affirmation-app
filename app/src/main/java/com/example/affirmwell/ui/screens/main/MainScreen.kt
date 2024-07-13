package com.example.affirmwell.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.data.Affirmation

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: AffirmationViewModel = viewModel(factory = AffirmationViewModel.Factory )
) {
    val affirmations by viewModel.affirmations.collectAsState()

    val pagerState = rememberPagerState(pageCount = {
        affirmations.size
    })

    Scaffold(
        topBar = {
            AffirmationTopBar()
        },
        bottomBar = {
            AffirmationBottomAppBar()
        }
    ) {
        AffirmationPager(
            affirmations = affirmations,
            pagerState = pagerState,
            modifier = Modifier.padding(it)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AffirmationPager(
    pagerState: PagerState,
    modifier: Modifier,
    affirmations: List<Affirmation>
) {
    VerticalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = affirmations[page].text,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun AffirmationBottomAppBar() {
    BottomAppBar(
        modifier = Modifier
            .background(Color.Transparent)
    ) {
        IconButton(onClick = { /* Handle menu click */ }) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }
        IconButton(onClick = { /* Handle menu click */ }) {
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Favourite")
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { /* Handle menu click */ }) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffirmationTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text("AffirmWell")},
        actions = {
            IconButton(onClick = { /* Handle menu click */ }) {
                Icon(Icons.Filled.Settings, contentDescription = "Menu")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    MainScreen()
}

@Preview
@Composable
fun MyBottomAppBarPreview() {
//    AffirmationBottomAppBar()
    AffirmationTopBar()
}