package com.example.systemsmodeling.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Seed = Color(0xFF1B4965)

private val LightColors = lightColorScheme(
    primary = Seed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCAE9FF),
    onPrimaryContainer = Color(0xFF001E2F),
    secondary = Color(0xFF00687A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFABEDFF),
    onSecondaryContainer = Color(0xFF001F26),
    tertiary = Color(0xFF006494),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFCAE6FF),
    onTertiaryContainer = Color(0xFF001E30),
    background = Color(0xFFF7F9FC),
    onBackground = Color(0xFF1A1C1E),
    surface = Color(0xFFF7F9FC),
    onSurface = Color(0xFF1A1C1E),
    surfaceVariant = Color(0xFFDDE3EA),
    onSurfaceVariant = Color(0xFF41484D),
    outline = Color(0xFF72787E),
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8ECFFF),
    onPrimary = Color(0xFF00344E),
    primaryContainer = Color(0xFF004B6E),
    onPrimaryContainer = Color(0xFFCAE9FF),
    secondary = Color(0xFF55D6F4),
    onSecondary = Color(0xFF003640),
    secondaryContainer = Color(0xFF004E5C),
    onSecondaryContainer = Color(0xFFABEDFF),
    tertiary = Color(0xFF8ECDFF),
    onTertiary = Color(0xFF00344F),
    tertiaryContainer = Color(0xFF004B70),
    onTertiaryContainer = Color(0xFFCAE6FF),
    background = Color(0xFF0F1419),
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF0F1419),
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF41484D),
    onSurfaceVariant = Color(0xFFC1C7CE),
    outline = Color(0xFF8B9198),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val scheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = scheme,
        typography = AppTypography,
        content = content
    )
}
