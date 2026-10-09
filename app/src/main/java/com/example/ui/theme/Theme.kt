package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = Color(0xFFD4AF37),
    secondary = Color(0xFF1E293B),
    tertiary = Color(0xFF94A3B8),
    background = Color(0xFF0A0A0A),
    surface = Color(0xFF111111),
    surfaceVariant = Color(0xFF1A1A1A),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFFF8FAFC),
    onTertiary = Color(0xFF111111),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFFD4AF37)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Color(0xFFD4AF37),
    secondary = Color(0xFF1E293B),
    tertiary = Color(0xFF94A3B8),
    background = Color(0xFF0A0A0A),
    surface = Color(0xFF111111),
    surfaceVariant = Color(0xFF1A1A1A),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFFF8FAFC),
    onTertiary = Color(0xFF111111),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFFD4AF37)
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
