package com.example.waterreminderapp

import kotlinx.coroutines.flow.Flow

class WaterIntakeRepository(private val waterIntakeDao: WaterIntakeDao) {

    val dailySummary: Flow<List<DailyWaterIntake>> = waterIntakeDao.getDailySummary()

    fun getForDate(date: String): Flow<List<WaterIntake>> {
        return waterIntakeDao.getForDate(date)
    }

    suspend fun insert(waterIntake: WaterIntake) {
        waterIntakeDao.insert(waterIntake)
    }

    suspend fun update(waterIntake: WaterIntake) {
        waterIntakeDao.update(waterIntake)
    }

    suspend fun delete(waterIntake: WaterIntake) {
        waterIntakeDao.deleteById(waterIntake.id)
    }

    // New function to call the DAO's deleteByDate
    suspend fun deleteByDate(date: String) {
        waterIntakeDao.deleteByDate(date)
    }
}