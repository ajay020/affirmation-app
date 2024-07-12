package com.example.affirmwell.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: AffirmationViewModel) {
    val affirmations by viewModel.affirmations.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val pagerState = rememberPagerState(pageCount = {
        affirmations.size
    })

    Scaffold(
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
    affirmations: List<String>
) {
    VerticalPager(
        modifier = modifier,
        state = pagerState
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = affirmations[page])
        }
    }
}

@Composable
fun AffirmationBottomAppBar() {
    BottomAppBar (
        modifier = Modifier
            .background(Color.Transparent)
    ){
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


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val viewModel = AffirmationViewModel()
    MainScreen(viewModel = viewModel)
}

@Preview
@Composable
fun MyBottomAppBarPreview() {
//    AffirmationBottomAppBar()
}