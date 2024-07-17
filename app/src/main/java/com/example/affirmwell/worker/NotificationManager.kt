package com.example.affirmwell.worker

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.affirmwell.data.UserPreferencesRepository
import com.example.affirmwell.utils.Utils.sliderValueToTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class AffirmationNotificationManager(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    val TAG: String = "NotificationManager"

    private val workManager = WorkManager.getInstance(context)

    fun scheduleNotifications() {

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            val numberOfNotifications =
                userPreferencesRepository.numberOfNotifications.first().coerceAtMost(6)
            val sliderValues  =  userPreferencesRepository.sliderValues.first()
            val startTime = sliderValueToTime(sliderValues.first)
            val endTime = sliderValueToTime(sliderValues.second)
            val selectedDays = userPreferencesRepository.selectedDays.first()
            val notificationsEnabled = userPreferencesRepository.notificationsEnabled.first()


            Log.d(TAG, "startTime: $startTime endTime: $endTime notificationEnabled $notificationsEnabled")

            val interval = calculateInterval(startTime, endTime, numberOfNotifications)
            val initialDelay = calculateInitialDelay(startTime, endTime)

            if (isTodaySelected(selectedDays)) {
                val workRequest =
                    PeriodicWorkRequestBuilder<NotificationWorker>(interval, TimeUnit.MINUTES)
                        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                        .addTag("Notification")
                        .build()


                Log.d(
                    TAG,
                    "Scheduled notification: Interval: ${TimeUnit.MILLISECONDS.toMinutes(interval)} minutes," +
                            " Initial Delay: $initialDelay ${
                                TimeUnit.MILLISECONDS.toMinutes(
                                    initialDelay
                                )
                            } minutes"
                )

                workManager.enqueueUniquePeriodicWork(
                    "AffirmationNotificationWork",
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            } else {
                Log.d(TAG, "Not Scheduled notification")
            }
        }

    }

    fun cancelNotifications() {
        workManager.cancelUniqueWork("AffirmationNotificationWork")
        workManager.cancelAllWorkByTag("testNotification")
    }

    private fun isTodaySelected(selectedDays: Set<String>): Boolean {
        val today = SimpleDateFormat("E", Locale.getDefault()).format(Date())
        Log.d("NotificationManager", "Today: $today Selected Days: $selectedDays")
        return selectedDays.contains(today)
    }


    private fun calculateInterval(
        startTime: String,
        endTime: String,
        numberOfNotifications: Int
    ): Long {
        // Parse start and end times
        val startMillis = parseTimeToMillis(startTime)
        val endMillis = parseTimeToMillis(endTime)
        // Calculate intervals
        val interval = ((endMillis - startMillis) / numberOfNotifications).coerceAtLeast(
            TimeUnit.MINUTES.toMillis(15)
        )

        return interval
    }

    private fun calculateInitialDelay(startTime: String, endTime: String): Long {
        val currentTime = System.currentTimeMillis()
        val startMillis = parseTimeToMillis(startTime)
        val endMillis = parseTimeToMillis(endTime)

        return when {
            currentTime < startMillis -> {
                // Current time is before start time, schedule for today
                startMillis - currentTime
            }
            currentTime in startMillis..endMillis -> {
                // Current time is between start time and end time, schedule immediately
                0L
            }
            else -> {
                // Current time is after end time, schedule for tomorrow
                startMillis + TimeUnit.DAYS.toMillis(1) - currentTime
            }
        }
    }

    private fun parseTimeToMillis(timeStr: String): Long {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = formatter.parse(timeStr) ?: return 0L
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, date.hours)
            set(Calendar.MINUTE, date.minutes)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

}
