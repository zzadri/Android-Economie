package com.example.projet1diiage.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Tes couleurs (déjà présentes dans Color.kt)
// NavyBlue, LightNavyBlue, PurpleGrey40/80, Pink40/80, etc.

private val LightColors = lightColorScheme(
    primary = NavyBlue,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    // (facultatif) surface/background si tu veux des valeurs explicites
    // background = Color(0xFFFFFFFF),
    // surface = Color(0xFFFFFFFF),
)

private val DarkColors = darkColorScheme(
    primary = LightNavyBlue,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    // background = Color(0xFF121212),
    // surface = Color(0xFF121212),
)

@Composable
fun AppTheme(
    useDynamicColor: Boolean = true,
    forceDarkTheme: Boolean? = null,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val darkTheme = forceDarkTheme ?: isSystemInDarkTheme()

    val colorScheme = when {
        useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Edge-to-edge conseillé : barre de statut transparente
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            // Icônes de statut claires en dark, sombres en light
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
