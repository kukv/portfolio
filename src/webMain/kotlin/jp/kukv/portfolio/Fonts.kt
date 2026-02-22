package jp.kukv.portfolio

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import portfolio.generated.resources.NotoSansJP_Black
import portfolio.generated.resources.NotoSansJP_Bold
import portfolio.generated.resources.NotoSansJP_ExtraBold
import portfolio.generated.resources.NotoSansJP_ExtraLight
import portfolio.generated.resources.NotoSansJP_Light
import portfolio.generated.resources.NotoSansJP_Medium
import portfolio.generated.resources.NotoSansJP_Regular
import portfolio.generated.resources.NotoSansJP_SemiBold
import portfolio.generated.resources.NotoSansJP_Thin
import portfolio.generated.resources.Res

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

@Composable
fun NotoSansJpFamily(): FontFamily =
    FontFamily(
        Font(Res.font.NotoSansJP_Thin, weight = FontWeight.Thin),
        Font(Res.font.NotoSansJP_ExtraLight, weight = FontWeight.ExtraLight),
        Font(Res.font.NotoSansJP_Light, weight = FontWeight.Light),
        Font(Res.font.NotoSansJP_Regular, weight = FontWeight.Normal),
        Font(Res.font.NotoSansJP_Medium, weight = FontWeight.Medium),
        Font(Res.font.NotoSansJP_SemiBold, weight = FontWeight.SemiBold),
        Font(Res.font.NotoSansJP_Bold, weight = FontWeight.Bold),
        Font(Res.font.NotoSansJP_ExtraBold, weight = FontWeight.ExtraBold),
        Font(Res.font.NotoSansJP_Black, weight = FontWeight.Black),
    )
