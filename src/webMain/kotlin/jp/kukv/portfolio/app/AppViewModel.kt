package jp.kukv.portfolio.app

import androidx.compose.runtime.Stable
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

enum class WindowSizeClass {
    Mobile,
    Tablet,
    Desktop,
}

@Stable
class WindowSizeState(windowSizeClass: WindowSizeClass) {
    var windowSizeClass by mutableStateOf(windowSizeClass)
        private set

    val isMobile: Boolean get() = windowSizeClass == WindowSizeClass.Mobile
    val isTablet: Boolean get() = windowSizeClass == WindowSizeClass.Tablet
    val isDesktop: Boolean get() = windowSizeClass == WindowSizeClass.Desktop

    internal fun update(sizeClass: WindowSizeClass) {
        windowSizeClass = sizeClass
    }
}

class AppViewModel : ViewModel() {
    var isDarkTheme by mutableStateOf(resolveDarkTheme(readStorage(THEME_KEY), prefersDarkColorScheme()))
        private set
    var language by mutableStateOf(resolveLanguage(readStorage(LANGUAGE_KEY), browserLanguage()))
        private set
    val windowSizeState = WindowSizeState(WindowSizeClass.Mobile)

    fun setDarkTheme(value: Boolean) {
        isDarkTheme = value
        writeStorage(THEME_KEY, themeStorageValue(value))
    }

    fun setLanguage(value: AppLanguage) {
        language = value
        writeStorage(LANGUAGE_KEY, value.tag)
    }

    fun updateWindowSize(sizeClass: WindowSizeClass) {
        windowSizeState.update(sizeClass)
    }

    private companion object {
        const val THEME_KEY = "theme"
        const val LANGUAGE_KEY = "language"
    }
}

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> { error("No AppViewModel provided") }
