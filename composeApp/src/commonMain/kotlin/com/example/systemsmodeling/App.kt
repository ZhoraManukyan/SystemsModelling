package com.example.systemsmodeling

import androidx.compose.runtime.Composable
import com.example.systemsmodeling.ui.navigation.MainNavHost
import com.example.systemsmodeling.ui.theme.AppTheme

@Composable
fun App() {
    AppTheme {
        MainNavHost()
    }
}
