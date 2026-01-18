package com.example.waterreminderapp

import android.app.Application

class WaterReminderApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { WaterIntakeRepository(database.waterIntakeDao()) }
}