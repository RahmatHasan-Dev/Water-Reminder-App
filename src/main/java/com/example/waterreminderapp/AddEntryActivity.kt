package com.example.waterreminderapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class AddEntryActivity : AppCompatActivity() {

    private val waterIntakeViewModel: WaterIntakeViewModel by viewModels {
        WaterIntakeViewModelFactory((application as WaterReminderApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        setupQuickAddButtons()
        setupCustomButton()
        setupNavigation()
    }

    private fun setupQuickAddButtons() {
        findViewById<LinearLayout>(R.id.btnAdd100ml).setOnClickListener { saveWaterIntake(100) }
        findViewById<LinearLayout>(R.id.btnAdd200ml).setOnClickListener { saveWaterIntake(200) }
        findViewById<LinearLayout>(R.id.btnAdd300ml).setOnClickListener { saveWaterIntake(300) }
        findViewById<LinearLayout>(R.id.btnAdd400ml).setOnClickListener { saveWaterIntake(400) }
        findViewById<LinearLayout>(R.id.btnAdd500ml).setOnClickListener { saveWaterIntake(500) }
    }

    private fun setupCustomButton() {
        findViewById<LinearLayout>(R.id.btnCustom).setOnClickListener {
            showCustomEntryDialog()
        }
    }

    private fun showCustomEntryDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_custom_entry)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialog.findViewById<Button>(R.id.btnSave)
        val etAmount = dialog.findViewById<EditText>(R.id.etAmount)

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSave.setOnClickListener {
            val amountString = etAmount.text.toString()
            if (amountString.isBlank()) {
                etAmount.error = getString(R.string.error_amount_empty)
                return@setOnClickListener
            }

            val amount = amountString.toIntOrNull()
            if (amount == null || amount <= 0) {
                etAmount.error = getString(R.string.error_enter_valid_amount)
                return@setOnClickListener
            }

            saveWaterIntake(amount)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun saveWaterIntake(amount: Int) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val waterIntake = WaterIntake(amount = amount, date = currentDate)
        waterIntakeViewModel.insert(waterIntake)

        Toast.makeText(this, R.string.toast_entry_saved, Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            finish() // Go back to the previous screen
        }, 1000)
    }

    private fun setupNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_add // Assuming this is the correct item for this activity

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
                R.id.nav_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.nav_add -> true
                else -> false
            }
        }
    }
}