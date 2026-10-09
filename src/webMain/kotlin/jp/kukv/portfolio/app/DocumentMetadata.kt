package jp.kukv.portfolio.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.lib.setDocumentLanguage
import jp.kukv.portfolio.shared.lib.setDocumentTitle
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.app_title

/** 表示中の言語を <html lang> と document.title に反映する。 */
@Composable
fun DocumentMetadata(language: AppLanguage) {
    val title = stringResource(Res.string.app_title)
    LaunchedEffect(language, title) {
        setDocumentLanguage(language.tag)
        setDocumentTitle(title)
    }
}
