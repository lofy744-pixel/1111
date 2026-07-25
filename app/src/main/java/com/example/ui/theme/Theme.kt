package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = CyberNeonPink,
  onPrimary = Color.White,
  primaryContainer = CyberSurfaceVariant,
  onPrimaryContainer = CyberNeonPink,
  secondary = CyberNeonCyan,
  onSecondary = Color.Black,
  secondaryContainer = CyberSurfaceVariant,
  onSecondaryContainer = CyberNeonCyan,
  tertiary = CyberNeonPurple,
  onTertiary = Color.White,
  background = CyberBackground,
  onBackground = CyberTextPrimary,
  surface = CyberSurface,
  onSurface = CyberTextPrimary,
  surfaceVariant = CyberSurfaceVariant,
  onSurfaceVariant = CyberTextSecondary,
  outline = CyberBorderPink
)

private val LightColorScheme = lightColorScheme(
  primary = CyberLightPrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFCE4EC),
  onPrimaryContainer = CyberLightPrimary,
  secondary = Color(0xFF00838F),
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFE0F7FA),
  onSecondaryContainer = Color(0xFF006064),
  tertiary = CyberNeonPurple,
  onTertiary = Color.White,
  background = CyberLightBackground,
  onBackground = CyberLightTextPrimary,
  surface = CyberLightSurface,
  onSurface = CyberLightTextPrimary,
  surfaceVariant = Color(0xFFE8EAF6),
  onSurfaceVariant = CyberLightTextSecondary,
  outline = Color(0xFFD81B60)
)

@Composable
fun NeovaTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

