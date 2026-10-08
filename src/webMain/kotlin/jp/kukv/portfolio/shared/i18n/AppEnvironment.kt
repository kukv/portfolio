@file:OptIn(ExperimentalWasmJsInterop::class)

package jp.kukv.portfolio.shared.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

/**
 * Compose Resources の言語をその場で切り替える。
 * 公式 API がまだ無いため、公式ドキュメントの回避策(index.html の navigator.languages 上書き)を使う。
 * 公式 API ができたら、このファイルだけを差し替える。
 */
@Composable
fun AppEnvironment(
    language: AppLanguage,
    content: @Composable () -> Unit,
) {
    applyCustomLocale(language.tag)
    key(language) {
        content()
    }
}

private fun applyCustomLocale(tag: String) {
    js(
        """
        if (window.__customLocale !== tag) {
            window.__customLocale = tag;
            window.dispatchEvent(new Event("languagechange"));
        }
        """,
    )
}
