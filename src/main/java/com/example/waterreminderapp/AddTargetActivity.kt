package com.example.waterreminderapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.card.MaterialCardView

class AddTargetActivity : AppCompatActivity() {

    private lateinit var tvCurrentTarget: TextView
    private lateinit var etNewTarget: EditText
    private lateinit var btnSaveTarget: Button
    private lateinit var card1000ml: MaterialCardView
    private lateinit var card2000ml: MaterialCardView
    private lateinit var card3000ml: MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_target)

        tvCurrentTarget = findViewById(R.id.tvCurrentTargetValue)
        etNewTarget = findViewById(R.id.etNewTarget)
        btnSaveTarget = findViewById(R.id.btnSaveTarget)
        card1000ml = findViewById(R.id.card1000ml)
        card2000ml = findViewById(R.id.card2000ml)
        card3000ml = findViewById(R.id.card3000ml)

        loadCurrentTarget()
        setupQuickSelectCards()
        setupSaveButton()
        setupNavigation()
    }

    private fun loadCurrentTarget() {
        val sharedPref = getSharedPreferences("WaterReminderPrefs", Context.MODE_PRIVATE)
        val currentTarget = sharedPref.getInt("daily_target", 2000)
        tvCurrentTarget.text = "$currentTarget ml"
    }

    private fun setupQuickSelectCards() {
        card1000ml.setOnClickListener {
            saveNewTarget(1000)
            showSuccessDialog()
        }
        card2000ml.setOnClickListener {
            saveNewTarget(2000)
            showSuccessDialog()
        }
        card3000ml.setOnClickListener {
            saveNewTarget(3000)
            showSuccessDialog()
        }
    }

    private fun setupSaveButton() {
        btnSaveTarget.setOnClickListener {
            val newTargetString = etNewTarget.text.toString()
            if (newTargetString.isNotBlank()) {
                val newTarget = newTargetString.toInt()
                saveNewTarget(newTarget)
                showSuccessDialog()
            } else {
                Toast.makeText(this, R.string.toast_enter_valid_target, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNewTarget(target: Int) {
        val sharedPref = getSharedPreferences("WaterReminderPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("daily_target", target)
            apply()
        }
        tvCurrentTarget.text = "$target ml"
        etNewTarget.text.clear()
    }

    private fun showSuccessDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_target_added)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            if (dialog.isShowing) {
                dialog.dismiss()
            }
            finish()
        }, 1500)
    }

    private fun setupNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.selectedItemId = R.id.nav_add

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
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