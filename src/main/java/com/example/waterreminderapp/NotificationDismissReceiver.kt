package com.example.waterreminderapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class NotificationDismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Just dismiss the notification with ID 1
        NotificationManagerCompat.from(context).cancel(1)
    }
}