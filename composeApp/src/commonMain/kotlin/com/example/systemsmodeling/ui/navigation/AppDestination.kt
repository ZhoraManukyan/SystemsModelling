package com.example.systemsmodeling.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.ui.graphics.vector.ImageVector

enum class AppDestination(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector
) {
    Lab1(
        title = "Лаб. 1 — случайные величины",
        shortLabel = "Лаб 1",
        icon = Icons.Filled.Functions
    ),
    Lab2(
        title = "Лаб. 2 — ЛКГ",
        shortLabel = "Лаб 2",
        icon = Icons.Filled.Calculate
    ),
    Lab4(
        title = "Лаб. 4 — непрерывные распределения",
        shortLabel = "Лаб 4",
        icon = Icons.Filled.ShowChart
    ),
    Lab5(
        title = "Лаб. 5 — дискретные распределения",
        shortLabel = "Лаб 5",
        icon = Icons.Filled.DeviceHub
    )
}
