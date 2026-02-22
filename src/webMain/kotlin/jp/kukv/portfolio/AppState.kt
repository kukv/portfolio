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

enum class WindowSizeClass {
    Mobile,
    Tablet,
    Desktop,
}

class AppState(
    isDarkTheme: Boolean,
    windowSizeClass: WindowSizeClass,
    windowHeight: Dp,
    val scrollState: ScrollState,
    val sectionPositions: SnapshotStateMap<String, Int>,
    val snackbarHostState: SnackbarHostState,
    val drawerState: androidx.compose.material3.DrawerState,
    val scope: CoroutineScope,
) {
    var isDarkTheme by mutableStateOf(isDarkTheme)
    var windowSizeClass by mutableStateOf(windowSizeClass)
    var windowHeight by mutableStateOf(windowHeight)

    val isMobile: Boolean get() = windowSizeClass == WindowSizeClass.Mobile
    val isTablet: Boolean get() = windowSizeClass == WindowSizeClass.Tablet
    val isDesktop: Boolean get() = windowSizeClass == WindowSizeClass.Desktop

    fun navigate(section: String) {
        scope.launch {
            val pos = sectionPositions[section] ?: 0
            scrollState.animateScrollTo(pos)
        }
    }
}

val LocalAppState = compositionLocalOf<AppState> { error("No AppState provided") }

@Composable
fun rememberAppState(): AppState {
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

    val scrollState = rememberScrollState()
    val sectionPositions = remember { mutableStateMapOf<String, Int>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val appState =
        remember {
            AppState(
                isDarkTheme = false,
                windowSizeClass = windowSizeClass,
                windowHeight = windowHeight,
                scrollState = scrollState,
                sectionPositions = sectionPositions,
                snackbarHostState = snackbarHostState,
                drawerState = drawerState,
                scope = scope,
            )
        }

    appState.windowSizeClass = windowSizeClass
    appState.windowHeight = windowHeight

    return appState
}
