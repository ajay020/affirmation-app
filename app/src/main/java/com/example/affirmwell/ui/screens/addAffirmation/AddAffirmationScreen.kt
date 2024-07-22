package com.example.affirmwell.ui.screens.addAffirmation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.data.Affirmation
import com.example.affirmwell.ui.theme.AffirmWellTheme

val TAG = "AddAffirmationScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAffirmationScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit,
    viewModel: AddAffirmationViewModel = viewModel(factory = AddAffirmationViewModel.FACTORY)
) {
    val affirmations by viewModel.affirmations.collectAsState()

    Log.d(TAG, "AddAffirmationScreen: affirmations: ${affirmations}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "My affirmations") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Menu")
                    }
                }
            )
        },

        ) {
        AddAffirmationBody(
            modifier = Modifier.padding(it),
            affirmations = affirmations,
            onDeleteAffirmation = { affirmation ->
                viewModel.deleteAffirmation(affirmation)
            },
            onUpdateAffirmation = { affirmation ->
                viewModel.updateAffirmation(affirmation)
            },
            onAddAffirmation = {
                viewModel.addAffirmation(it)
            }
        )
    }
}

@Composable
fun AddAffirmationBody(
    modifier: Modifier = Modifier,
    affirmations: List<Affirmation>,
    onUpdateAffirmation: (Affirmation) -> Unit,
    onDeleteAffirmation: (Affirmation) -> Unit,
    onAddAffirmation: (Affirmation) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    AddAffirmationDialog(
        affirmation = null,
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onEdit = {},
        onSave = { affirmationText ->
            showDialog = false
            onAddAffirmation(
                Affirmation(
                    category = "My Affirmations",
                    isCustom = true,
                    text = affirmationText
                )
            )
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (affirmations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(4f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You haven't added any affirmation.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            AddAffirmationList(
                modifier = Modifier
                    .weight(4f),
                affirmations = affirmations,
                onDeleteAffirmation = onDeleteAffirmation,
                onUpdateAffirmation = onUpdateAffirmation
            )
        }
        Button(
            modifier = Modifier
                .padding(8.dp),
            onClick = {
                showDialog = true
            }
        ) {
            Text(text = "Add Affirmation")
        }
    }
}

@Composable
fun AddAffirmationList(
    modifier: Modifier = Modifier,
    affirmations: List<Affirmation>,
    onDeleteAffirmation: (Affirmation) -> Unit,
    onUpdateAffirmation: (Affirmation) -> Unit
) {
    var showOptionDialog by remember {
        mutableStateOf(false)
    }

    var showEditDialog by remember {
        mutableStateOf(false)
    }

    var selectedAffirmation by remember {
        mutableStateOf<Affirmation?>(null)
    }

    // Display dialog for edit affirmation
    AddAffirmationDialog(
        affirmation = selectedAffirmation,
        showDialog = showEditDialog,
        onDismiss = { showEditDialog = false },
        onEdit = {
            onUpdateAffirmation(it)
        },
        onSave = {}
    )

    // Display dialog for actions
    AffirmationActionDialog(
        affirmation = selectedAffirmation,
        showDialog = showOptionDialog,
        onDismiss = { showOptionDialog = false },
        onDelete = { affirmation ->
            onDeleteAffirmation(affirmation)
        },
        onUpdate = {
            showEditDialog = true
            showOptionDialog = false
        },
        onSaveToFavorite = { affirmation ->
            onUpdateAffirmation(affirmation)
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(affirmations) { affirmation ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = affirmation.text,
                        modifier = Modifier
                            .weight(4f)
                            .padding(8.dp)
                    )
                    IconButton(
                        onClick = {
                            showOptionDialog = true
                            selectedAffirmation = affirmation
                        },
                        modifier = Modifier
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "more"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AffirmationActionDialog(
    affirmation: Affirmation?,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onDelete: (Affirmation) -> Unit,
    onUpdate: () -> Unit,
    onSaveToFavorite: (Affirmation) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.medium,
//                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 24.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Save to favorites",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (affirmation != null) {
                                    onSaveToFavorite(
                                        affirmation.copy(isFavorite = !affirmation.isFavorite)
                                    )
                                }
                                onDismiss()
                            }
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Edit",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (affirmation != null) {
                                    onUpdate()
                                }
                                onDismiss()
                            }
                            .padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Delete",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (affirmation != null) {
                                    onDelete(affirmation)
                                }
                                onDismiss()
                            }
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun AddAffirmationDialog(
    affirmation: Affirmation?,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    onEdit: (Affirmation) -> Unit
) {
    // Note: remember initialise the value of a variable only once. Here, with key field, it will reset when
    // the value of affirmation changes.
    var affirmationText by remember(key1 = affirmation?.id) {
        mutableStateOf(
            affirmation?.text ?: ""
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                if (affirmation != null) {
                    Text("Edit Affirmation")
                } else {
                    Text("Add Affirmation")
                }
            },
            text = {
                Column {
                    TextField(
                        value = affirmationText,
                        onValueChange = { affirmationText = it },
                        label = { Text("Enter affirmation") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (affirmation != null) {
                        onEdit(
                            affirmation.copy(text = affirmationText)
                        )
                    } else {
                        onSave(affirmationText)
                    }
                    onDismiss()
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AddAffirmationScreenPreview() {

//    AddAffirmationDialog(
//        showDialog = true,
//        onDismiss = {},
//        onSave = {},
//        onEdit = {},
//        affirmation = null
//    )

//    AffirmationActionDialog(
//        affirmation = Affirmation(
//            id = 0,
//            category = "General",
//            isCustom = true,
//            isFavorite = false,
//            text = "All you have is NOW"
//        ),
//        showDialog = true,
//        onDismiss = { /*TODO*/ },
//        onDelete = {},
//        onUpdate = {},
//        onSaveToFavorite = {}
//    )

//    AffirmationActionDialog(
//        affirmation = Affirmation(text = "Hello", category = "General"),
//        showDialog = true,
//        onDismiss = { /*TODO*/ },
//        onDelete = {}
//    ) {
//
//    }

    val affirmations = listOf(
        Affirmation(
            id = 0,
            category = "General",
            isCustom = true,
            isFavorite = false,
            text = "All you have is NOW"
        ),
        Affirmation(
            id = 0,
            category = "General",
            isCustom = true,
            isFavorite = false,
            text = "All you have is now"
        ),
        Affirmation(
            id = 0,
            category = "General",
            isCustom = true,
            isFavorite = false,
            text = "All you have is now"
        ),
    )

    AffirmWellTheme (
        darkTheme = true
    ){
        AddAffirmationBody(
            affirmations = emptyList(),
            onAddAffirmation = {},
            onUpdateAffirmation = {},
            onDeleteAffirmation = {}
        )
    }


//    AddAffirmationList(affirmations = affirmations)
}