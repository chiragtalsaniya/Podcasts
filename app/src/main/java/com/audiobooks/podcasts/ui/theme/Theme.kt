package com.audiobooks.podcasts.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Light color scheme
private val LightColorScheme = lightColorScheme(
    primary = Red,
    onPrimary = Color.White,
    secondary = Black,
    onSecondary = Color.White,
    background = Color.White,
    surface = Color.White,
    onBackground = Black,
    onSurface = Black
)

// Dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Red,
    onPrimary = Black,
    secondary = Black,
    onSecondary = Color.White,
    background = Black,
    surface = Black,
    onBackground = Color.White,
    onSurface = Color.White
)


@Composable
fun PodcastsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true, content: @Composable () -> Unit
) {
    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, content = content
    )
}