package com.priyabrata.gymapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.priyabrata.gymapp.ui.navigation.AppNavigation
import com.priyabrata.gymapp.ui.theme.GymAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Makes status bar transparent/edge-to-edge
        setContent {
            GymAppTheme {  // ← Uses YOUR theme (dark bg, cyan accent, status bar fix)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background  // ← Dark background globally
                ) {
                    AppNavigation()
                }
            }
        }
    }
}