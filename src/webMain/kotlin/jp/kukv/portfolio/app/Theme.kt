package jp.kukv.portfolio.app

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.theme.PortfolioTypography
import jp.kukv.portfolio.shared.theme.changeColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    viewModel: AppViewModel,
    content: @Composable () -> Unit,
) {
    val colorScheme = changeColorScheme(viewModel.isDarkTheme)
    val shapes = Shapes(largeIncreased = RoundedCornerShape(24.dp))

    CompositionLocalProvider(LocalAppViewModel provides viewModel) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            shapes = shapes,
            typography = PortfolioTypography(),
            content = content,
        )
    }
}
