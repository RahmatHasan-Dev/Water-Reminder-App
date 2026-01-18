package com.example.waterreminderapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private val waterIntakeViewModel: WaterIntakeViewModel by viewModels {
        WaterIntakeViewModelFactory((application as WaterReminderApplication).repository)
    }

    private lateinit var tvCurrentIntake: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var progressBar: ProgressBar

    private var currentIntake = 0
    private var dailyTarget = 2000 // Default target

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCurrentIntake = findViewById(R.id.tvCurrentIntake)
        tvSubtitle = findViewById(R.id.tvSubtitle)
        progressBar = findViewById(R.id.progressBarWater)

        setupNavigation()
        createNotificationChannel()
        scheduleReminder()
        observeTodayIntake()
    }

    override fun onResume() {
        super.onResume()
        loadTargetAndUpdateUI()
    }

    private fun loadTargetAndUpdateUI() {
        val sharedPref = getSharedPreferences("WaterReminderPrefs", Context.MODE_PRIVATE)
        dailyTarget = sharedPref.getInt("daily_target", 2000)
        updateUI()
    }

    private fun observeTodayIntake() {
        waterIntakeViewModel.getTodayIntake().observe(this, Observer {
            currentIntake = it.sumOf { entry -> entry.amount }
            updateUI()
        })
    }

    private fun updateUI() {
        tvCurrentIntake.text = currentIntake.toString()

        val percentage = if (dailyTarget > 0) {
            (currentIntake * 100) / dailyTarget
        } else {
            0
        }

        progressBar.progress = percentage
        tvSubtitle.text = getString(R.string.percentage_format, percentage, dailyTarget)
    }

    private fun setupNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_edit -> {
                    startActivity(Intent(this, EditEntryActivity::class.java))
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddTargetActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val btnAddWater = findViewById<ImageButton>(R.id.btnAddWater)
        btnAddWater.setOnClickListener {
            startActivity(Intent(this, AddEntryActivity::class.java))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("water_reminder_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleReminder() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, // Repeats daily
            pendingIntent
        )
    }
}