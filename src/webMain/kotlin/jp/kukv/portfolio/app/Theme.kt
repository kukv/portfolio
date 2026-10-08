package jp.kukv.portfolio.app

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import jp.kukv.portfolio.shared.theme.PortfolioTypography
import jp.kukv.portfolio.shared.theme.changeColorScheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    viewModel: AppViewModel,
    content: @Composable () -> Unit,
) {
    val colorScheme = changeColorScheme(viewModel.isDarkTheme)

    CompositionLocalProvider(LocalAppViewModel provides viewModel) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            motionScheme = MotionScheme.expressive(),
            typography = PortfolioTypography(),
            content = content,
        )
    }
}
