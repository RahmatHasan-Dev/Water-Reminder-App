package com.example.waterreminderapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WaterIntake::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterIntakeDao(): WaterIntakeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "water_reminder_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}