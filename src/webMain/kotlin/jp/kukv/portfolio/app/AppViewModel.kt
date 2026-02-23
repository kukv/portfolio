package jp.kukv.portfolio.app

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel

enum class WindowSizeClass {
    Mobile,
    Tablet,
    Desktop,
}

class WindowSizeState(windowSizeClass: WindowSizeClass, windowHeight: Dp) {
    var windowSizeClass by mutableStateOf(windowSizeClass)
    var windowHeight by mutableStateOf(windowHeight)

    val isMobile: Boolean get() = windowSizeClass == WindowSizeClass.Mobile
    val isTablet: Boolean get() = windowSizeClass == WindowSizeClass.Tablet
    val isDesktop: Boolean get() = windowSizeClass == WindowSizeClass.Desktop
}

class AppViewModel : ViewModel() {
    var isDarkTheme by mutableStateOf(false)
    val windowSizeState = WindowSizeState(WindowSizeClass.Desktop, 0.dp)

    fun updateWindowSize(
        sizeClass: WindowSizeClass,
        height: Dp,
    ) {
        windowSizeState.windowSizeClass = sizeClass
        windowSizeState.windowHeight = height
    }
}

val LocalAppViewModel = staticCompositionLocalOf<AppViewModel> { error("No AppViewModel provided") }
