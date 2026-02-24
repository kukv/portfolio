package jp.kukv.portfolio.app

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.kukv.portfolio.shared.layout.DesktopLayout
import jp.kukv.portfolio.shared.layout.MobileLayout

@Composable
fun App() {
    val viewModel: AppViewModel = viewModel { AppViewModel() }

    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val windowWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val windowSizeClass =
        when {
            windowWidth < 600.dp -> WindowSizeClass.Mobile
            windowWidth <= 893.dp -> WindowSizeClass.Tablet
            else -> WindowSizeClass.Desktop
        }

    LaunchedEffect(windowSizeClass) {
        viewModel.updateWindowSize(windowSizeClass)
    }

    val scrollState = remember { ScrollState(0) }
    val sectionPositions = remember { mutableStateMapOf<String, Int>() }
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme(viewModel) {
        when {
            viewModel.windowSizeState.isMobile -> MobileLayout(scrollState, sectionPositions, snackbarHostState)
            else -> DesktopLayout(scrollState, sectionPositions, snackbarHostState)
        }
    }
}
