package com.example.waterreminderapp

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = 1
        val channelId = "water_reminder_channel"

        // Intent for "Yes" action -> Opens MainActivity
        val yesIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val yesPendingIntent = PendingIntent.getActivity(
            context,
            0,
            yesIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent for "No" action -> Dismisses notification (handled automatically by system usually, 
        // but we can add a dummy receiver to do nothing or track stats)
        val noIntent = Intent(context, NotificationDismissReceiver::class.java)
        val noPendingIntent = PendingIntent.getBroadcast(
            context, 
            1, 
            noIntent, 
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Ensure this icon exists or use standard drawable
            .setContentTitle("Water Reminder")
            .setContentText("Have you had a drink yet?")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setAutoCancel(true)
            .addAction(R.drawable.ic_check_rounded, "Yes", yesPendingIntent) // Using check icon for yes
            .addAction(0, "No", noPendingIntent) // No icon for No
            .setColor(Color.parseColor("#2563EB"))

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(notificationId, notificationBuilder.build())
            }
        }
    }
}