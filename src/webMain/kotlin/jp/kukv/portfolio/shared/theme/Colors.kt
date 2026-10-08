package jp.kukv.portfolio.shared.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// 主色 #8C1D2C(ワインレッド)、tertiary #B85C38(テラコッタ)から生成した Material 3 のカラーロール。
val WineLightColorScheme =
    lightColorScheme(
        primary = Color(0xFF8C1D2C),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFFFDADA),
        onPrimaryContainer = Color(0xFF891A2A),
        inversePrimary = Color(0xFFFFB3B4),
        secondary = Color(0xFF815153),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFDBFC0),
        onSecondaryContainer = Color(0xFF7A4B4C),
        tertiary = Color(0xFF9A4523),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFDBCF),
        onTertiaryContainer = Color(0xFF7B2F0E),
        background = Color(0xFFFFF8F7),
        onBackground = Color(0xFF221919),
        surface = Color(0xFFFFF8F7),
        onSurface = Color(0xFF221919),
        surfaceVariant = Color(0xFFF8DCDC),
        onSurfaceVariant = Color(0xFF554242),
        surfaceTint = Color(0xFF8C1D2C),
        inverseSurface = Color(0xFF382E2E),
        inverseOnSurface = Color(0xFFFFEDEC),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF93000A),
        outline = Color(0xFF887272),
        outlineVariant = Color(0xFFDBC0C0),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFFFFF8F7),
        surfaceDim = Color(0xFFE8D6D5),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFFF0F0),
        surfaceContainer = Color(0xFFFCEAE9),
        surfaceContainerHigh = Color(0xFFF6E4E3),
        surfaceContainerHighest = Color(0xFFF0DEDE),
    )

val WineDarkColorScheme =
    darkColorScheme(
        primary = Color(0xFFFFB3B4),
        onPrimary = Color(0xFF680017),
        primaryContainer = Color(0xFF8C1D2C),
        onPrimaryContainer = Color(0xFFFFDADA),
        inversePrimary = Color(0xFFA9333F),
        secondary = Color(0xFFF4B7B8),
        onSecondary = Color(0xFF4C2526),
        secondaryContainer = Color(0xFF693D3E),
        onSecondaryContainer = Color(0xFFE5A9AA),
        tertiary = Color(0xFFFFB59A),
        onTertiary = Color(0xFF5B1B00),
        tertiaryContainer = Color(0xFF7E3110),
        onTertiaryContainer = Color(0xFFFFDBCF),
        background = Color(0xFF1A1111),
        onBackground = Color(0xFFF0DEDE),
        surface = Color(0xFF1A1111),
        onSurface = Color(0xFFF0DEDE),
        surfaceVariant = Color(0xFF554242),
        onSurfaceVariant = Color(0xFFDBC0C0),
        surfaceTint = Color(0xFFFFB3B4),
        inverseSurface = Color(0xFFF0DEDE),
        inverseOnSurface = Color(0xFF382E2E),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        outline = Color(0xFFA38B8B),
        outlineVariant = Color(0xFF554242),
        scrim = Color(0xFF000000),
        surfaceBright = Color(0xFF413736),
        surfaceDim = Color(0xFF1A1111),
        surfaceContainerLowest = Color(0xFF140C0C),
        surfaceContainerLow = Color(0xFF221919),
        surfaceContainer = Color(0xFF271D1D),
        surfaceContainerHigh = Color(0xFF322827),
        surfaceContainerHighest = Color(0xFF3D3232),
    )

fun changeColorScheme(isDarkTheme: Boolean): ColorScheme =
    when (isDarkTheme) {
        true -> WineDarkColorScheme
        false -> WineLightColorScheme
    }
