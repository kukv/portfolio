package jp.kukv.portfolio.ui.styles

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

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
