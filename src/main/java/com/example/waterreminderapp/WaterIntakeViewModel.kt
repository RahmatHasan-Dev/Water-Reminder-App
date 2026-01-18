package com.example.waterreminderapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WaterIntakeViewModel(private val repository: WaterIntakeRepository) : ViewModel() {

    val dailySummary: LiveData<List<DailyWaterIntake>> = repository.dailySummary.asLiveData()

    fun getTodayIntake(): LiveData<List<WaterIntake>> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val today = sdf.format(Date())
        return repository.getForDate(today).asLiveData()
    }

    // This function was missing
    fun getForDate(date: String): LiveData<List<WaterIntake>> {
        return repository.getForDate(date).asLiveData()
    }

    fun insert(waterIntake: WaterIntake) = viewModelScope.launch {
        repository.insert(waterIntake)
    }

    fun update(waterIntake: WaterIntake) = viewModelScope.launch {
        repository.update(waterIntake)
    }

    fun delete(waterIntake: WaterIntake) = viewModelScope.launch {
        repository.delete(waterIntake)
    }

    fun deleteByDate(date: String) = viewModelScope.launch {
        repository.deleteByDate(date)
    }
}