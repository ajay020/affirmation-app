package com.example.affirmwell.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.affirmwell.R
import com.example.affirmwell.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerDialog(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            decorFitsSystemWindows = true,

            )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            TopAppBar(
                title = { Text("Category") },
                navigationIcon = {
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp)
            ) {
                items(categories) { category ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(8.dp)

                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .border(
                                    width = 2.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (selectedCategory?.name == category.name) Color.Black else Color.Transparent
                                )
                                .clickable {
                                    onCategorySelected(category)
                                },
                        ) {
                            Box (
                                contentAlignment = Alignment.BottomCenter,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black)
                            ){
                                Image(
                                    painter = painterResource(id = category.imgRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(
                                            brush = Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.7f)
                                                ),
                                            )
                                        )
                                )
                                Text(
                                    text = category.name,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding( bottom = 12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CategoryPickerPreview() {

    CategoryPickerDialog(
        categories = listOf(
            Category(R.drawable.grad1, name = "General"),
            Category(R.drawable.grad2, name = "Health"),
            Category(R.drawable.grad3, name = "Anxiety"),
            Category(R.drawable.grad1, name = "General"),
            Category(R.drawable.grad2, name = "Health"),
        ),
        selectedCategory = Category(R.drawable.grad1, name = "Anxiety"),
        onCategorySelected = {},
        onDismissRequest = {}
    )

}