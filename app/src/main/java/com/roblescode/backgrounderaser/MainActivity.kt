package com.roblescode.backgrounderaser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.roblescode.backgrounderaser.ui.screens.BackgroundEraserScreen
import com.roblescode.backgrounderaser.ui.theme.BackgroundEraserTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BackgroundEraserTheme {
                BackgroundEraserScreen()
            }
        }
    }
}
