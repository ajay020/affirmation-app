package com.example.affirmwell.ui.screens.main

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.R
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.model.Category
import com.example.affirmwell.utils.Utils

val TAG = "MainScreen"

@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {
    val affirmations by viewModel.affirmations.collectAsState()
    val backgroundImageRes by viewModel.backgroundImageRes.collectAsState(initial = R.drawable.img1)
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Log.d(TAG, "MainScreen: run called ")

    LaunchedEffect(selectedCategory?.name) {
        viewModel.loadAffirmationsByCategory(selectedCategory?.name ?: "")
    }

    Scaffold(
        topBar = {},
        bottomBar = {}
    ) {
        if (affirmations.isNotEmpty()) {

            MainScreenContent(
                modifier = Modifier
                    .padding(it),
                backgroundImageRes = backgroundImageRes,
                affirmations = affirmations,
                onNavigateToSettings = onNavigateToSettings,
                selectedCategory = selectedCategory,
                saveBackgroundImageInDataStore = { imageRes ->
                    viewModel.saveBackgroundImageInDataStore(imageRes)
                },
                saveCategoryInDataStore = { category ->
                    viewModel.saveCategoryInDataStore(category)
                },

                onToggleFavourite = {
                    viewModel.toggleFavorite(it)
                }
            )
        } else {
            // Show a loading indicator or empty state
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    modifier: Modifier = Modifier,
    backgroundImageRes: Int,
    affirmations: List<Affirmation>,
    onNavigateToSettings: () -> Unit,
    selectedCategory: Category?,
    saveBackgroundImageInDataStore: (Int) -> Unit,
    saveCategoryInDataStore: (Category) -> Unit,
    onToggleFavourite: (Affirmation) -> Unit
) {
    Log.d(TAG, "MainScreenContent: run called ")

    var showImagePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = {
            affirmations.size
        }
    )

    if (showImagePicker) {
        FullScreenImagePickerDialog(
            images = Utils.images,
            onDismissRequest = { showImagePicker = false },
            onImageSelected = { imageRes ->
                saveBackgroundImageInDataStore(imageRes)
            }
        )
    }

    if (showCategoryPicker) {
        CategoryPickerDialog(
            categories = Utils.catagories,
            selectedCategory = selectedCategory,
            onCategorySelected = { category ->
                saveCategoryInDataStore(category)
            },
            onDismissRequest = { showCategoryPicker = false }
        )
    }

    Box(
        modifier = modifier
    ) {
        backgroundImageRes.let {
            Image(
                painter = painterResource(id = backgroundImageRes),
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
                    .weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onNavigateToSettings) {
                Icon(Icons.Filled.Settings, contentDescription = "Menu")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            AffirmationBottomAppBar(
                affirmation = affirmations[pagerState.currentPage],
                onToggleFavorite = { onToggleFavourite(affirmations[pagerState.currentPage]) },
                onSelectBackgroundImageClick = { showImagePicker = true },
                selectedCategory = selectedCategory,
                onCategoryClick = { showCategoryPicker = true }
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
//                .background(
//                    brush = Brush.verticalGradient(
//                        colors = listOf(Color.Transparent, Color.Black),
//                        startY = 0f,
//                        endY = Float.POSITIVE_INFINITY
//                    )
//                )
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = affirmations[page].text,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AffirmationBottomAppBar(
    modifier: Modifier = Modifier,
    affirmation: Affirmation,
    onToggleFavorite: (Affirmation) -> Unit,
    onSelectBackgroundImageClick: () -> Unit,
    selectedCategory: Category?,
    onCategoryClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp),
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
        IconButton(onClick = { onSelectBackgroundImageClick() }) {
            Icon(Icons.Default.Face, contentDescription = "Menu")
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onCategoryClick() }) {
            Icon(Icons.Filled.Face, contentDescription = "Category")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "${selectedCategory?.name}")
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


@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val affirmations = listOf(
        Affirmation(
            text = "This is an example affirmation",
            category = "Anxiety",
            isFavorite = false
        ),
    )
    MainScreenContent(
        backgroundImageRes = R.drawable.grad1,
        affirmations = affirmations,
        onNavigateToSettings = {},
        selectedCategory = null,
        saveBackgroundImageInDataStore = {},
        saveCategoryInDataStore = {},
        onToggleFavourite = {}
    )
}

@Preview
@Composable
fun MyBottomAppBarPreview() {
//    AffirmationBottomAppBar(
//        affirmation = Affirmation(
//            text = "This is an example affirmation",
//            category = "Anxiety",
//            isFavorite = false
//        ),
//        onToggleFavorite = {},
//        onClick = {},
//        selectedCategory = Category(R.drawable.grad1, name = "General"),
//        onCategoryClick = {}
//    )
//    AffirmationTopBar()
}