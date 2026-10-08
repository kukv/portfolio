@file:OptIn(ExperimentalWasmJsInterop::class)

package jp.kukv.portfolio.shared.lib

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

fun readStorage(key: String): String? = js("window.localStorage.getItem(key)")

fun writeStorage(
    key: String,
    value: String,
) {
    js("window.localStorage.setItem(key, value)")
}

fun prefersDarkColorScheme(): Boolean = js("window.matchMedia('(prefers-color-scheme: dark)').matches")

/** Task 7 で上書きする navigator.languages ではなく、ブラウザ本来の言語を返す。 */
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
