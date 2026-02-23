package jp.kukv.portfolio.app

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ── Window Size ────────────────────────────────────────────────────────────

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

// ── AppViewModel ───────────────────────────────────────────────────────────

class AppViewModel : ViewModel() {
    // テーマ
    var isDarkTheme by mutableStateOf(false)

    // ウィンドウサイズ（App() から毎フレーム更新される）
    val windowSizeState = WindowSizeState(WindowSizeClass.Desktop, 0.dp)

    // ナビゲーション
    val scrollState = ScrollState(0)
    val sectionPositions = mutableStateMapOf<String, Int>()
    val drawerState = DrawerState(DrawerValue.Closed)
    val snackbarHostState = SnackbarHostState()

    fun navigate(section: String) {
        viewModelScope.launch {
            val pos = sectionPositions[section] ?: 0
            scrollState.animateScrollTo(pos)
        }
    }
}

val LocalAppViewModel = compositionLocalOf<AppViewModel> { error("No AppViewModel provided") }
