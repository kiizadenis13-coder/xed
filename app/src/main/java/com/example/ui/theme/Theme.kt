package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// High-Octane Black and Emerald Green Theme
private val BlackAndGreenColorScheme = darkColorScheme(
    primary = PrimaryBrand,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0D381E),
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = AccentGreen,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0A2B18),
    onSecondaryContainer = Color(0xFF6EE7B7),
    tertiary = MintGreen,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF062112),
    onTertiaryContainer = Color(0xFFD1FAE5),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Color.Transparent,
    outlineVariant = Color.Transparent,
    error = AccentRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBrand,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF00381B),
    secondary = AccentGreen,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFA7F3D0),
    onSecondaryContainer = Color(0xFF00381B),
    tertiary = ForestGreen,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF00381B),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = Color.Transparent,
    outlineVariant = Color.Transparent,
    error = AccentRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to deep black and vibrant emerald green palette
    dynamicColor: Boolean = false, // Preserve iconic emerald green branding
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> BlackAndGreenColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
