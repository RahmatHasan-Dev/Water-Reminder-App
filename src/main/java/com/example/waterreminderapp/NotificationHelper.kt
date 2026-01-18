package com.example.waterreminderapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    fun scheduleReminder(context: Context) {
        val channelId = "water_reminder"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Water Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_water_drop_24) // Corrected icon
            .setContentTitle("Water Reminder")
            .setContentText("Have you had a drink yet?")
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1001, notification)
    }
}
