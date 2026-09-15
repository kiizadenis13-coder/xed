package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppSplashScreen
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.dashboard.DashboardViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                var showSplash by remember { mutableStateOf(true) }
                val dashboardViewModel: DashboardViewModel = viewModel()

                Crossfade(targetState = showSplash, label = "splash_crossfade") { isSplash ->
                    if (isSplash) {
                        AppSplashScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    } else {
                        DashboardScreen(viewModel = dashboardViewModel)
                    }
                }
            }
        }
    }
}
