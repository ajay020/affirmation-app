package com.example.affirmwell.ui.screens.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.R
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.model.Category
import com.example.affirmwell.ui.theme.AffirmWellTheme
import com.example.affirmwell.utils.Utils
import kotlinx.coroutines.launch

val TAG = "MainScreen"

@Composable
fun MainScreen(
    onNavigateToSettings: () -> Unit,
    viewModel: MainScreenViewModel = viewModel(factory = MainScreenViewModel.Factory)
) {

    val affirmations by viewModel.affirmations.collectAsState()
    val backgroundImageRes by viewModel.backgroundImageRes.collectAsState(initial = R.drawable.img1)
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(selectedCategory?.name) {
        viewModel.loadAffirmationsByCategory(selectedCategory?.name ?: "")
    }

    Log.d(TAG, "MainScreen: affirmations: ${affirmations.size}")

    Scaffold {
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
            },
            onShareClick = { affirmation ->
                coroutineScope.launch {
                    Utils.shareTextOverImage(context, affirmation.text, backgroundImageRes)
                    Toast.makeText(context, "Sharing affirmation...", Toast.LENGTH_SHORT).show()
                }
            }
        )
//        } else {
//            // Show a loading indicator or empty state
//            Box(
//                modifier = Modifier
//                    .padding(it)
//                    .fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
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
    onToggleFavourite: (Affirmation) -> Unit,
    onShareClick: (Affirmation) -> Unit
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
        Image(
            painter = painterResource(id = backgroundImageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

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
                affirmation = if (affirmations.isNotEmpty()) affirmations[pagerState.currentPage] else null,
                onToggleFavorite = { onToggleFavourite(affirmations[pagerState.currentPage]) },
                onSelectBackgroundImageClick = { showImagePicker = true },
                selectedCategory = selectedCategory,
                onCategoryClick = { showCategoryPicker = true },
                onShareClick = onShareClick
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
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 50f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = affirmations[page].text,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun AffirmationBottomAppBar(
    modifier: Modifier = Modifier,
    affirmation: Affirmation?,
    onToggleFavorite: (Affirmation) -> Unit,
    onSelectBackgroundImageClick: () -> Unit,
    selectedCategory: Category?,
    onCategoryClick: () -> Unit,
    onShareClick: (Affirmation) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 16.dp),
    ) {
        if (affirmation != null) {
            IconButton(
                onClick = { onShareClick(affirmation) },
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp) // Adjust the size as needed
                        .background(Color.White, shape = CircleShape)
                        .border(1.dp, Color.Gray, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Share,
                        contentDescription = "Share",
                        tint = Color.DarkGray
                    )
                }
            }
            IconButton(onClick = { onToggleFavorite(affirmation) }) {
                Box(
                    modifier = Modifier
                        .size(48.dp) // Adjust the size as needed
                        .background(Color.White, shape = CircleShape)
                        .border(1.dp, Color.Gray, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (affirmation.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = Color.DarkGray
                    )
                }
            }
        }

        IconButton(onClick = { onSelectBackgroundImageClick() }) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(1.dp, Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_wallpaper),
                    contentDescription = "Change Background",
                    tint = Color.DarkGray
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = { onCategoryClick() },
            colors = ButtonDefaults.outlinedButtonColors(Color.Black.copy(alpha = 0.1f)),
        ) {
            Icon(
                Icons.Filled.Face,
                contentDescription = "Category",
                tint = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${selectedCategory?.name}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
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

    AffirmWellTheme(
        darkTheme = true
    ) {
        MainScreenContent(
            backgroundImageRes = R.drawable.grad1,
            affirmations = affirmations,
            onNavigateToSettings = {},
            selectedCategory = null,
            saveBackgroundImageInDataStore = {},
            saveCategoryInDataStore = {},
            onToggleFavourite = {},
            onShareClick = {}
        )
    }

}
