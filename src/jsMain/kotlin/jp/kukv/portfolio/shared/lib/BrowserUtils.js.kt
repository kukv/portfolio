package jp.kukv.portfolio.shared.lib

import kotlinx.browser.window

actual fun openUrl(url: String) {
    window.open(url, "_blank")
}
