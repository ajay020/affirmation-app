package com.example.affirmwell

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.affirmwell.ui.navigation.AffirmationNavigation
import com.example.affirmwell.ui.theme.AffirmWellTheme

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AffirmWellTheme {
                AffirmationNavigation()
            }
        }
    }
}
