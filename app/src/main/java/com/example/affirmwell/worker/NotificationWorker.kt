package com.example.affirmwell.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.affirmwell.R
import com.example.affirmwell.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random


class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val userPreferencesRepository: UserPreferencesRepository
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = "NotificationWorker"

    override suspend fun doWork(): Result {

        // Get the current day
        val currentDay = SimpleDateFormat("E", Locale.getDefault()).format(Date())

        // Retrieve the selected days from DataStore
        val selectedDays = runBlocking {
            userPreferencesRepository.selectedDays.first()
        }

        // Check if the current day is one of the selected days
        if (selectedDays.contains(currentDay)) {
            Log.d(TAG, "Worker: send notifications");
            sendNotification()
        } else {
            Log.d(TAG, "Worker: not send notifications");
        }
        return Result.success()
    }

    private fun sendNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random().nextInt()
        val channelId = "affirmation_channel"

        // Create the notification channel (if it doesn't already exist)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Affirmations",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for affirmation notifications"
            }
            // Check if the channel already exists
            if (notificationManager.getNotificationChannel(channelId) == null) {
                notificationManager.createNotificationChannel(channel)
            }
        }

        // Build the notification using NotificationCompat.Builder
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Daily Affirmation")
            .setContentText(getAffirmation())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    private fun getAffirmation(): String {
        // Retrieve an affirmation from your data source
        return "You are strong and capable!"
    }
}

