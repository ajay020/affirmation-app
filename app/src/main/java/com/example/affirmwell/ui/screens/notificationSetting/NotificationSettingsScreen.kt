package com.example.affirmwell.ui.screens.notificationSetting

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.affirmwell.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    viewModel: NotificationSettingsViewModel = viewModel(factory = NotificationSettingsViewModel.Factory),
    onNavigateBack: () -> Unit
) {
    val numberOfNotifications by viewModel.numberOfNotifications.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val selectedDays by viewModel.selectedDays.collectAsState()
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val sliderPosition by viewModel.sliderPosition.collectAsState()

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
        SettingsContent(
            modifier = Modifier.padding(paddingValues),
            startTime = startTime,
            endTime = endTime,
            numberOfNotifications = numberOfNotifications,
            selectedDays = selectedDays,
            sliderPosition = sliderPosition,
            notificationsEnabled = notificationsEnabled,
            toggleNotificationEnabled = { viewModel.toggleNotificationsEnabled() },
            onSliderValueChange = { range ->
                viewModel.onSliderValueChange(start = range.start, end = range.endInclusive)
            },
            setNumberOfNotifications = { viewModel.setNumberOfNotifications(it) },
            toggleDays = { viewModel.toggleDay(it) }

        )
    }
}

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    startTime: String,
    endTime: String,
    selectedDays: Set<String>,
    numberOfNotifications: Int,
    sliderPosition: Pair<Float, Float>,
    notificationsEnabled: Boolean,
    toggleNotificationEnabled: () -> Unit,
    onSliderValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    setNumberOfNotifications: (Int) -> Unit,
    toggleDays: (String) -> Unit

) {
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
//                .background(Color.Gray)
        ) {
            Text("Enable Notifications")
            Switch(
                checked = notificationsEnabled,
                onCheckedChange = { toggleNotificationEnabled() },
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        // Slider for selecting start and end times
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
//                .background(Color.Gray)
        ) {
            Text(text = "Start Time:  $startTime")
            Text(text = "End Time:  $endTime")
        }
        Spacer(modifier = Modifier.height(16.dp))


        RangeSlider(
            value = sliderPosition.first..sliderPosition.second,
            onValueChange = onSliderValueChange,
            valueRange = 0f..24f,  // Assuming time range from 0 to 24 hours
            steps = 23,  // To divide slider into 1-hour intervals,
            enabled = notificationsEnabled,
            modifier = Modifier
                .fillMaxWidth()
//                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.height(16.dp))

        NumberOfNotificationsInput(
            numberOfNotifications = numberOfNotifications,
            onNumberChange = setNumberOfNotifications
        )
        Spacer(modifier = Modifier.height(16.dp))

        DaysOfWeekSelector(
            selectedDays = selectedDays,
            onDayToggle = toggleDays
        )

    }
}

@Composable
fun NumberOfNotificationsInput(
    numberOfNotifications: Int,
    onNumberChange: (Int) -> Unit
) {
    Column {
        OutlinedTextField(
            value = numberOfNotifications.toString(),
            label = {
                Text("Number of Notifications")
            },
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
    val daysOfWeek = Constants.DAYS_OF_WEEK_ABBR

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach { day ->
            val isSelected = selectedDays.contains(day)
            val backgroundColor = if (isSelected) Color.Blue else Color.Gray
            val contentColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .clickable { onDayToggle(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day,
                    color = contentColor,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationsSettingsPreview() {
    SettingsContent(
        startTime = "8:00",
        endTime = "20:00",
        selectedDays = setOf("Mon", "Tue"),
        numberOfNotifications = 3,
        sliderPosition = 2f to 4f,
        notificationsEnabled = true,
        toggleNotificationEnabled = { /*TODO*/ },
        onSliderValueChange = {},
        setNumberOfNotifications = {}
    ) {

    }
}

@Preview
@Composable
private fun DaysWeekSelectorPreview() {
//    DaysOfWeekSelector(selectedDays = setOf("Mon", "Tue")) {
//
//    }
}
