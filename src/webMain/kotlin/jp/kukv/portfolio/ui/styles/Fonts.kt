package jp.kukv.portfolio.ui.styles

import androidx.compose.runtime.Composable
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
