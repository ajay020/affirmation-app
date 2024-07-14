package com.example.affirmwell.ui.screens.notificationSetting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    viewModel: NotificationSettingsViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val numberOfNotifications by viewModel.numberOfNotifications.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val selectedDays by viewModel.selectedDays.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            NumberOfNotificationsInput(
                numberOfNotifications = numberOfNotifications,
                onNumberChange = { viewModel.setNumberOfNotifications(it) }
            )
            TimeInput(
                label = "Start Time",
                time = startTime,
                onTimeChange = { viewModel.setStartTime(it) }
            )
            TimeInput(
                label = "End Time",
                time = endTime,
                onTimeChange = { viewModel.setEndTime(it) }
            )
            DaysOfWeekSelector(
                selectedDays = selectedDays,
                onDayToggle = { viewModel.toggleDay(it) }
            )
            Button(
                onClick = { viewModel.saveSettings() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Save Settings")
            }
        }
    }
}

@Composable
fun NumberOfNotificationsInput(
    numberOfNotifications: Int,
    onNumberChange: (Int) -> Unit
) {
    Column {
        Text("Number of Notifications")
        TextField(
            value = numberOfNotifications.toString(),
            onValueChange = { value ->
                val number = value.toIntOrNull() ?: 0
                onNumberChange(number)
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun TimeInput(
    label: String,
    time: String,
    onTimeChange: (String) -> Unit
) {
    Column {
        Text(label)
        TextField(
            value = time,
            onValueChange = onTimeChange,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun DaysOfWeekSelector(
    selectedDays: Set<String>,
    onDayToggle: (String) -> Unit
) {
    val daysOfWeek =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Column {
        Text("Days of the Week")
        daysOfWeek.forEach { day ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDayToggle(day) }
                    .padding(vertical = 8.dp)
            ) {
                Checkbox(
                    checked = selectedDays.contains(day),
                    onCheckedChange = { onDayToggle(day) }
                )
                Text(day)
            }
        }
    }
}
