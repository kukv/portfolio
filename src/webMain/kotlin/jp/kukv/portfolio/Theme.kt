package jp.kukv.portfolio

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    appState: AppState,
    content: @Composable () -> Unit,
) {
    val colorScheme = changeColorScheme(appState.theme.isDarkTheme)
    val shapes = Shapes(largeIncreased = RoundedCornerShape(24.dp))

    CompositionLocalProvider(LocalAppState provides appState) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = PortfolioTypography(),
            content = content,
        )
    }
}
