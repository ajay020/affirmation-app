package com.example.affirmwell.ui.screens.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Favorite
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.R
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.utils.Utils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    viewModel: AffirmationViewModel = viewModel(factory = AffirmationViewModel.Factory)
) {
    val affirmations by viewModel.affirmations.collectAsState()
    val selectedBackgroundImage = remember { mutableStateOf<Int?>(null) }
    var showImagePicker by remember { mutableStateOf(false) }


    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = {
            affirmations.size
        }
    )

    Scaffold(
        topBar = {
            AffirmationTopBar()
        },
        bottomBar = {
            if (affirmations.isNotEmpty()) {
                AffirmationBottomAppBar(
                    affirmation = affirmations[pagerState.currentPage],
                    onToggleFavorite = { viewModel.toggleFavorite(it) },
                    onClick = { showImagePicker = true }
                )
            }

        }
    ) {
        if (affirmations.isNotEmpty()) {
            Box(
                modifier = Modifier
            ) {
                selectedBackgroundImage.value?.let { imageRes ->
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Column {
                    AffirmationPager(
                        affirmations = affirmations,
                        pagerState = pagerState,
                        modifier = Modifier
                            .padding(it)
                            .weight(1f)
                    )
                }
            }

        } else {
            // Show a loading indicator or empty state
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No affirmations available.")
            }
        }

        if (showImagePicker) {
            FullScreenImagePickerDialog(
                images = Utils.images,
                onDismissRequest = { showImagePicker = false },
                onImageSelected = { imageRes ->
                    selectedBackgroundImage.value = imageRes
                }
            )
        }
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
fun AffirmationBottomAppBar(
    modifier: Modifier = Modifier,
    affirmation: Affirmation,
    onToggleFavorite: (Affirmation) -> Unit,
    onClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .background(Color.Transparent)
    ) {
        IconButton(onClick = { /* Handle menu click */ }) {
            Icon(Icons.Filled.Share, contentDescription = "Share")
        }
        IconButton(onClick = { onToggleFavorite(affirmation) }) {
            Icon(
                if (affirmation.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favourite"
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { onClick() }) {
            Icon(Icons.Filled.Menu, contentDescription = "Menu")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullScreenImagePickerDialog(
    images: List<Int>,
    onDismissRequest: () -> Unit,
    onImageSelected: (Int) -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column {
                TopAppBar(
                    title = { Text("Select Background Image") },
                    navigationIcon = {
                        IconButton(onClick = { onDismissRequest() }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                )
                BackgroundImagePicker(
                    images = images,
                    onImageSelected = { imageRes ->
                        onImageSelected(imageRes)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AffirmationTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text("AffirmWell") },
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
//    AffirmationTopBar()
}