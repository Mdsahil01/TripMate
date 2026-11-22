package com.mdsahil.tripmate.ui.theme

import android.app.Activity
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

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = Color.White,
    primaryContainer = SurfaceVariantMint,
    onPrimaryContainer = EmeraldDark,
    secondary = EmeraldLight,
    onSecondary = Color.White,
    tertiary = CategoryTransport,
    background = SoftMintBackground,
    onBackground = TextDarkSlate,
    surface = SurfaceWhite,
    onSurface = TextDarkSlate,
    surfaceVariant = SurfaceVariantMint,
    onSurfaceVariant = TextMuted
)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = TextDarkSlate,
    primaryContainer = EmeraldDark,
    onPrimaryContainer = SurfaceVariantMint,
    secondary = EmeraldPrimary,
    onSecondary = Color.White,
    tertiary = CategoryTransport,
    background = DarkSlateCard,
    onBackground = Color.White,
    surface = DarkSlateCard,
    onSurface = Color.White,
    surfaceVariant = EmeraldDark,
    onSurfaceVariant = Color.LightGray
)

@Composable
fun TripMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
