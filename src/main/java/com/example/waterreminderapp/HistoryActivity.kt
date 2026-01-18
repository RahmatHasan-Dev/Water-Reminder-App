package com.example.waterreminderapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {

    private val waterIntakeViewModel: WaterIntakeViewModel by viewModels {
        WaterIntakeViewModelFactory((application as WaterReminderApplication).repository)
    }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setupRecyclerView()

        waterIntakeViewModel.dailySummary.observe(this, Observer {
            it?.let { historyAdapter.submitList(it) }
        })

        setupNavigation()
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(
            onItemClicked = { dailyWaterIntake ->
                val intent = Intent(this, EditEntryActivity::class.java).apply {
                    putExtra(EditEntryActivity.EXTRA_DATE, dailyWaterIntake.date)
                }
                startActivity(intent)
            },
            onDeleteClicked = { dailyWaterIntake ->
                showDeleteConfirmationDialog(dailyWaterIntake.date)
            }
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistory)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = historyAdapter
    }

    private fun showDeleteConfirmationDialog(date: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title_delete_history)
            .setMessage(getString(R.string.dialog_message_delete_history, date))
            .setPositiveButton(R.string.dialog_action_delete) { _, _ ->
                waterIntakeViewModel.deleteByDate(date)
                Toast.makeText(this, getString(R.string.toast_history_for_date_deleted, date), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.dialog_action_cancel, null)
            .show()
    }

    private fun setupNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_history

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    true
                }
                R.id.nav_edit -> {
                    startActivity(Intent(this, EditEntryActivity::class.java))
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddEntryActivity::class.java))
                    true
                }
                R.id.nav_history -> true // Already here
                else -> false
            }
        }
    }
}