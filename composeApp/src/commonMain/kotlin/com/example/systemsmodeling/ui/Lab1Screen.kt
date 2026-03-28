package com.example.systemsmodeling.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.systemsmodeling.ui.lab1.Lab1CombinationScreen
import com.example.systemsmodeling.ui.lab1.Lab1SegmentScreen

@Composable
fun Lab1Screen() {
    var tabIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize()) {
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 },
                text = {
                    Text(
                        "Сегментация",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                text = {
                    Text(
                        "Комбинации",
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }

        when (tabIndex) {
            0 -> Lab1SegmentScreen()
            else -> Lab1CombinationScreen()
        }
    }
}
