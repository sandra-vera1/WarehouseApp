package com.example.warehouseapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Brown,
    onPrimary = TextPrimary80,
    secondary = RoseLight,
    onSecondary = TextSecondary80,
    tertiary = RoseLight,
    tertiaryContainer = RedLight,
    background = Background80,
    outline = White,
    surface = Background80
)

private val LightColorScheme = lightColorScheme(
    primary = Brown,
    onPrimary = White,
    secondary = White,
    onSecondary = Brown,
    tertiary = RoseLight,
    tertiaryContainer = RedLight,
    background = White,
    outline = Background80,
    surface = White
)

@Composable
fun WarehouseAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}