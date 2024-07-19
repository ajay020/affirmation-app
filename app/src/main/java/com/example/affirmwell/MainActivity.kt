package com.example.affirmwell

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.affirmwell.ui.navigation.AffirmationApp
import com.example.affirmwell.ui.theme.AffirmWellTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AffirmWellTheme {
                AffirmationApp()
            }
        }
    }
}
