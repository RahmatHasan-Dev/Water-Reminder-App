package com.example.waterreminderapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake")
data class WaterIntake(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Int,
    val date: String
)