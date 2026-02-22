package jp.kukv.portfolio.ui.styles

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PortfolioTypography(): Typography {
    val fontFamily = NotoSansJpFamily()
    val defaults = Typography()
    return Typography(
        displayLarge = defaults.displayLarge.withFont(fontFamily),
        displayMedium = defaults.displayMedium.withFont(fontFamily),
        displaySmall = defaults.displaySmall.withFont(fontFamily),
        headlineLarge = defaults.headlineLarge.withFont(fontFamily),
        headlineMedium = defaults.headlineMedium.withFont(fontFamily),
        headlineSmall = defaults.headlineSmall.withFont(fontFamily),
        titleLarge = defaults.titleLarge.withFont(fontFamily),
        titleMedium = defaults.titleMedium.withFont(fontFamily),
        titleSmall = defaults.titleSmall.withFont(fontFamily),
        bodyLarge = defaults.bodyLarge.withFont(fontFamily),
        bodyMedium = defaults.bodyMedium.withFont(fontFamily),
        bodySmall = defaults.bodySmall.withFont(fontFamily),
        labelLarge = defaults.labelLarge.withFont(fontFamily),
        labelMedium = defaults.labelMedium.withFont(fontFamily),
        labelSmall = defaults.labelSmall.withFont(fontFamily),
    )
}

private fun TextStyle.withFont(fontFamily: FontFamily): TextStyle = copy(fontFamily = fontFamily)
