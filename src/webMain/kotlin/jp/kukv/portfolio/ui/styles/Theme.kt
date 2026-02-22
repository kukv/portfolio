package jp.kukv.portfolio.ui.styles

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private fun changeColorScheme(isDarkTheme: Boolean): ColorScheme =
    when (isDarkTheme) {
        true -> WineBaseDarkColorScheme
        false -> WineBaseLightColorScheme
    }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = changeColorScheme(isDarkTheme)
    val shapes = Shapes(largeIncreased = RoundedCornerShape(24.dp))

    MaterialExpressiveTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = PortfolioTypography(),
        content = content,
    )
}
