package jp.kukv.portfolio.shared.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Wine Red Palette
val WineRed10 = Color(0xFF3B0006)
val WineRed20 = Color(0xFF680012)
val WineRed40 = Color(0xFF8F001D) // Primary Light
val WineRed80 = Color(0xFFFFB3B4) // Primary Dark
val WineRed90 = Color(0xFFFFDAD9)

// Secondary Palette (Muted Red-Brown)
val Rosetta20 = Color(0xFF44292A)
val Rosetta40 = Color(0xFF775656)
val Rosetta80 = Color(0xFFE7BDBE)
val Rosetta90 = Color(0xFFFFDAD9)

// Neutral Palette (Warm Greys)
val WarmGrey10 = Color(0xFF1A1111) // Background Dark
val WarmGrey20 = Color(0xFF231919) // OnBackground Light
val WarmGrey90 = Color(0xFFF0DFDF) // OnBackground Dark
val WarmGrey98 = Color(0xFFFFF8F7) // Background Light

// Neutral Variant (For Surfaces/Outlines)
val VariantGrey30 = Color(0xFF534343)
val VariantGrey60 = Color(0xFF857373)
val VariantGrey80 = Color(0xFFD8C2C2)
val VariantGrey90 = Color(0xFFF4DDDD)

val WineBaseLightColorScheme =
    lightColorScheme(
        primary = WineRed40,
        onPrimary = Color.White,
        primaryContainer = WineRed90,
        onPrimaryContainer = WineRed10,
        secondary = Rosetta40,
        onSecondary = Color.White,
        secondaryContainer = Rosetta90,
        onSecondaryContainer = Rosetta20,
        background = WarmGrey98,
        onBackground = WarmGrey20,
        surface = WarmGrey98,
        onSurface = WarmGrey20,
        surfaceVariant = VariantGrey90,
        onSurfaceVariant = VariantGrey30,
        outline = VariantGrey60,
    )

val WineBaseDarkColorScheme =
    darkColorScheme(
        primary = WineRed80,
        onPrimary = WineRed20,
        primaryContainer = WineRed40,
        onPrimaryContainer = WineRed90,
        secondary = Rosetta80,
        onSecondary = Rosetta20,
        secondaryContainer = Rosetta20, // 暗め
        onSecondaryContainer = Rosetta90,
        background = WarmGrey10,
        onBackground = WarmGrey90,
        surface = WarmGrey10,
        onSurface = WarmGrey90,
        surfaceVariant = VariantGrey30,
        onSurfaceVariant = VariantGrey80,
        outline = VariantGrey60,
    )

fun changeColorScheme(isDarkTheme: Boolean): ColorScheme =
    when (isDarkTheme) {
        true -> WineBaseDarkColorScheme
        false -> WineBaseLightColorScheme
    }
