package com.example.taskmanager.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PurpleGlow,
    onPrimary = TextPrimary,
    primaryContainer = PurpleSoft,
    onPrimaryContainer = TextPrimary,
    secondary = CyanAccent,
    onSecondary = DeepNavy,
    secondaryContainer = CardSurfaceElevated,
    onSecondaryContainer = TextPrimary,
    tertiary = PinkAccent,
    onTertiary = TextPrimary,
    background = DeepNavy,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = CardSurface,
    onSurfaceVariant = TextSecondary,
    outline = TextMuted,
    error = PinkAccent,
    onError = TextPrimary
)

private val LightColorScheme = lightColorScheme(
    primary = PurpleLightTheme,
    onPrimary = SurfaceLight,
    primaryContainer = CardLight,
    onPrimaryContainer = TextOnLight,
    secondary = CyanAccent,
    onSecondary = SurfaceLight,
    secondaryContainer = CardLight,
    onSecondaryContainer = TextOnLight,
    tertiary = PinkAccent,
    onTertiary = SurfaceLight,
    background = BackgroundLight,
    onBackground = TextOnLight,
    surface = SurfaceLight,
    onSurface = TextOnLight,
    surfaceVariant = CardLight,
    onSurfaceVariant = TextMuted,
    outline = TextMuted,
    error = PinkAccent,
    onError = SurfaceLight
)

@Composable
fun TaskManagerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}