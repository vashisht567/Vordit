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

private val DarkColorScheme =
  darkColorScheme(
    primary = SaffronLight,
    onPrimary = Color.White,
    primaryContainer = SaffronDark,
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = IndigoLight,
    onSecondary = Color.White,
    tertiary = SandstoneGold,
    background = Color(0xFF1B1412),
    surface = Color(0xFF261D19),
    onBackground = Color(0xFFEDE0D9),
    onSurface = Color(0xFFEDE0D9),
    surfaceVariant = Color(0xFF382C27),
    onSurfaceVariant = Color(0xFFD6C4BC),
    outline = Color(0xFF6B584E),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RoyalSaffron,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE6DE),
    onPrimaryContainer = Color(0xFF3E1103),
    secondary = RoyalIndigo,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE0E5FF),
    onSecondaryContainer = Color(0xFF0D1754),
    tertiary = OasisGreen,
    onTertiary = Color.White,
    background = WarmCanvas,
    surface = WarmCard,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = WarmBorder,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
