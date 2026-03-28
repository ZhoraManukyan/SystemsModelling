package com.example.systemsmodeling.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.systemsmodeling.ui.Lab1Screen
import com.example.systemsmodeling.ui.Lab2Screen
import com.example.systemsmodeling.ui.Lab4Screen
import com.example.systemsmodeling.ui.Lab5Screen
import com.example.systemsmodeling.ui.components.AppTopBar

@Composable
fun MainNavHost() {
    var destinationKey by rememberSaveable { mutableStateOf(AppDestination.Lab1.name) }
    val destination =
        AppDestination.entries.find { it.name == destinationKey } ?: AppDestination.Lab1

    Scaffold(
        topBar = {
            AppTopBar(title = destination.title)
        },
        bottomBar = {
            NavigationBar {
                AppDestination.entries.forEach { dest ->
                    NavigationBarItem(
                        selected = destination == dest,
                        onClick = { destinationKey = dest.name },
                        icon = {
                            Icon(
                                imageVector = dest.icon,
                                contentDescription = dest.shortLabel
                            )
                        },
                        label = { Text(dest.shortLabel) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            when (destination) {
                AppDestination.Lab1 -> Lab1Screen()
                AppDestination.Lab2 -> Lab2Screen()
                AppDestination.Lab4 -> Lab4Screen()
                AppDestination.Lab5 -> Lab5Screen()
            }
        }
    }
}
