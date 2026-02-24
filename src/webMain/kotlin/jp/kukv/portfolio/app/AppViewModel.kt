package jp.kukv.portfolio.app

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel

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
    var isDarkTheme by mutableStateOf(false)
        private set
    val windowSizeState = WindowSizeState(WindowSizeClass.Mobile)

    fun setDarkTheme(value: Boolean) {
        isDarkTheme = value
    }

    fun updateWindowSize(sizeClass: WindowSizeClass) {
        windowSizeState.update(sizeClass)
    }
}

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> { error("No AppViewModel provided") }
