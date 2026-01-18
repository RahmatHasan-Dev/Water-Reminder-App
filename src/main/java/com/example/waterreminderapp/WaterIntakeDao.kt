package com.example.waterreminderapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterIntakeDao {
    @Insert
    suspend fun insert(waterIntake: WaterIntake)

    @Update
    suspend fun update(waterIntake: WaterIntake)

    @Query("DELETE FROM water_intake WHERE id = :id")
    suspend fun deleteById(id: Int)

    // New function to delete all entries for a specific date
    @Query("DELETE FROM water_intake WHERE SUBSTR(date, 1, 10) = :date")
    suspend fun deleteByDate(date: String)

    @Query("SELECT SUBSTR(date, 1, 10) as date, SUM(amount) as totalAmount FROM water_intake GROUP BY SUBSTR(date, 1, 10) ORDER BY date DESC")
    fun getDailySummary(): Flow<List<DailyWaterIntake>>

    @Query("SELECT * FROM water_intake WHERE date LIKE :date || '%' ORDER BY date DESC")
    fun getForDate(date: String): Flow<List<WaterIntake>>
}