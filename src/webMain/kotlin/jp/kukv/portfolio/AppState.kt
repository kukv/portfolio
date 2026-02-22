package jp.kukv.portfolio

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// ── Theme ──────────────────────────────────────────────────────────────────

class ThemeState(isDarkTheme: Boolean = false) {
    var isDarkTheme by mutableStateOf(isDarkTheme)
}

@Composable
fun rememberThemeState(isDarkTheme: Boolean = false): ThemeState = remember { ThemeState(isDarkTheme) }

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

@Composable
fun rememberWindowSizeState(): WindowSizeState {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val windowWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val windowHeight = with(density) { windowInfo.containerSize.height.toDp() }
    val windowSizeClass =
        when {
            windowWidth < 600.dp -> WindowSizeClass.Mobile
            windowWidth <= 893.dp -> WindowSizeClass.Tablet
            else -> WindowSizeClass.Desktop
        }
    val state = remember { WindowSizeState(windowSizeClass, windowHeight) }
    state.windowSizeClass = windowSizeClass
    state.windowHeight = windowHeight
    return state
}

// ── Navigation ─────────────────────────────────────────────────────────────

class NavigationState(
    val scrollState: ScrollState,
    val sectionPositions: SnapshotStateMap<String, Int>,
    val drawerState: androidx.compose.material3.DrawerState,
    val snackbarHostState: SnackbarHostState,
    val scope: CoroutineScope,
) {
    fun navigate(section: String) {
        scope.launch {
            val pos = sectionPositions[section] ?: 0
            scrollState.animateScrollTo(pos)
        }
    }
}

@Composable
fun rememberNavigationState(): NavigationState {
    val scrollState = rememberScrollState()
    val sectionPositions = remember { mutableStateMapOf<String, Int>() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    return remember { NavigationState(scrollState, sectionPositions, drawerState, snackbarHostState, scope) }
}

// ── App State ──────────────────────────────────────────────────────────────

class AppState(
    val theme: ThemeState,
    val windowSize: WindowSizeState,
    val navigation: NavigationState,
)

val LocalAppState = compositionLocalOf<AppState> { error("No AppState provided") }

@Composable
fun rememberAppState(): AppState {
    val theme = rememberThemeState()
    val windowSize = rememberWindowSizeState()
    val navigation = rememberNavigationState()
    return remember { AppState(theme, windowSize, navigation) }
}
