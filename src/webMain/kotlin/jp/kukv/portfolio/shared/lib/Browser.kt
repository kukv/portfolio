@file:OptIn(ExperimentalWasmJsInterop::class)

package jp.kukv.portfolio.shared.lib

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

// localStorage はブラウザ設定やプライベートモードによってアクセス自体が例外になるため、
// 読めなければ未保存扱い、書けなければ保存しないだけにする。
fun readStorage(key: String): String? =
    js("(function () { try { return window.localStorage.getItem(key); } catch (e) { return null; } })()")

fun writeStorage(
    key: String,
    value: String,
) {
    js("try { window.localStorage.setItem(key, value); } catch (e) {}")
}

fun prefersDarkColorScheme(): Boolean = js("window.matchMedia('(prefers-color-scheme: dark)').matches")

/** index.html と AppEnvironment で上書きする navigator.languages ではなく、ブラウザ本来の言語を返す。 */
fun browserLanguage(): String = js("window.navigator.language")

fun setDocumentLanguage(tag: String) {
    js("document.documentElement.lang = tag")
}

fun setDocumentTitle(title: String) {
    js("document.title = title")
}

fun removeElementById(id: String) {
    js("var element = document.getElementById(id); if (element) element.remove()")
}
