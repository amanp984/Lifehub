package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = OnPrimaryDark,
    secondary = AccentBlue,
    background = DarkBackground,
    surface = DarkCardSurface,
    onBackground = OnSurfaceDark,
    onSurface = OnSurfaceDark,
    outline = DarkCardBorder,
    tertiary = AccentMint
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    secondary = BluePrimary,
    background = LightBackground,
    surface = LightCardSurface,
    onBackground = OnSurfaceLight,
    onSurface = OnSurfaceLight,
    outline = LightCardBorder,
    tertiary = AccentMint
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We enforce an elite twilight-dark theme by default for that premium Apple-inspired experience,
    // but support standard dynamic color mappings if requested.
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
