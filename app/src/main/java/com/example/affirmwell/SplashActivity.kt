package com.example.affirmwell

import android.os.Bundle
import android.content.Intent
import androidx.activity.ComponentActivity

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start the main activity
        startActivity(Intent(this, MainActivity::class.java))
        // Close the splash activity
        finish()
    }
}
