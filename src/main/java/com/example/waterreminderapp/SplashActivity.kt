package com.example.waterreminderapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DURATION: Long = 5000 // Total 5 detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val glassLogo = findViewById<ImageView>(R.id.imgGlassLogo)
        val waterDrop = findViewById<ImageView>(R.id.imgWaterDrop)

        // Make drop visible before animation
        waterDrop.visibility = View.VISIBLE

        // Load gentle and ripple animations
        val dropAnim = AnimationUtils.loadAnimation(this, R.anim.drop_fall_gentle)
        val rippleAnim = AnimationUtils.loadAnimation(this, R.anim.glass_ripple)

        // Start animations
        waterDrop.startAnimation(dropAnim)
        glassLogo.startAnimation(rippleAnim)

        // Handler to navigate to MainActivity after 5 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DURATION)
    }
}