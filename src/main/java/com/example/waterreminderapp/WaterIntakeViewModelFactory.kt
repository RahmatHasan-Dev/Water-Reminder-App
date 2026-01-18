package com.example.waterreminderapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class WaterIntakeViewModelFactory(private val repository: WaterIntakeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WaterIntakeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WaterIntakeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}