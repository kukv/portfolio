package jp.kukv.portfolio.shared.lib

import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(url) => { window.open(url, '_blank'); }")
private external fun wasmOpenUrl(url: String)

actual fun openUrl(url: String) {
    wasmOpenUrl(url)
}
