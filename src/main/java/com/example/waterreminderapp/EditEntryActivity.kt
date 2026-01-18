package com.example.waterreminderapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class EditEntryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
    }

    private val waterIntakeViewModel: WaterIntakeViewModel by viewModels {
        WaterIntakeViewModelFactory((application as WaterReminderApplication).repository)
    }

    private lateinit var editEntryAdapter: EditEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_entry)

        setupRecyclerView()

        val dateToShow = intent.getStringExtra(EXTRA_DATE)

        val entries = if (dateToShow != null) {
            waterIntakeViewModel.getForDate(dateToShow)
        } else {
            waterIntakeViewModel.getTodayIntake()
        }

        entries.observe(this, Observer {
            it?.let { editEntryAdapter.submitList(it) }
        })

        setupNavigation()
    }

    private fun setupRecyclerView() {
        editEntryAdapter = EditEntryAdapter(
            onEditClicked = { waterIntake -> showEditDialog(waterIntake) },
            onDeleteClicked = { waterIntake -> showDeleteDialog(waterIntake) }
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewEntries)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = editEntryAdapter
    }

    private fun showEditDialog(waterIntake: WaterIntake) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_entry)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val etAmount = dialog.findViewById<EditText>(R.id.etAmount)
        etAmount.setText(waterIntake.amount.toString())

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSave.setOnClickListener {
            val amountString = etAmount.text.toString()
            if (amountString.isBlank()) {
                etAmount.error = getString(R.string.error_amount_empty)
                return@setOnClickListener
            }
            val newAmount = amountString.toIntOrNull()
            if (newAmount != null && newAmount > 0) {
                val updatedIntake = waterIntake.copy(amount = newAmount)
                waterIntakeViewModel.update(updatedIntake)
                Toast.makeText(this, R.string.toast_entry_updated, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                etAmount.error = getString(R.string.error_enter_valid_amount)
            }
        }

        dialog.show()
    }

    private fun showDeleteDialog(waterIntake: WaterIntake) {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_title_delete_entry)
            .setMessage(R.string.dialog_message_delete_entry)
            .setPositiveButton(R.string.dialog_action_delete) { _, _ ->
                waterIntakeViewModel.delete(waterIntake)
                Toast.makeText(this, R.string.toast_entry_deleted, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.dialog_action_cancel, null)
            .show()
    }

    private fun setupNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_edit

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    true
                }
                R.id.nav_edit -> true
                R.id.nav_add -> {
                    startActivity(Intent(this, AddEntryActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}