package jp.kukv.portfolio.app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.i18n.resolveLanguage
import jp.kukv.portfolio.shared.lib.browserLanguage
import jp.kukv.portfolio.shared.lib.prefersDarkColorScheme
import jp.kukv.portfolio.shared.lib.readStorage
import jp.kukv.portfolio.shared.lib.writeStorage
import jp.kukv.portfolio.shared.theme.resolveDarkTheme
import jp.kukv.portfolio.shared.theme.themeStorageValue

class AppViewModel : ViewModel() {
    var isDarkTheme by mutableStateOf(resolveDarkTheme(readStorage(THEME_KEY), prefersDarkColorScheme()))
        private set
    var language by mutableStateOf(resolveLanguage(readStorage(LANGUAGE_KEY), browserLanguage()))
        private set

    fun setDarkTheme(value: Boolean) {
        isDarkTheme = value
        writeStorage(THEME_KEY, themeStorageValue(value))
    }

    fun setLanguage(value: AppLanguage) {
        language = value
        writeStorage(LANGUAGE_KEY, value.tag)
    }

    private companion object {
        const val THEME_KEY = "theme"
        const val LANGUAGE_KEY = "language"
    }
}

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> { error("No AppViewModel provided") }
