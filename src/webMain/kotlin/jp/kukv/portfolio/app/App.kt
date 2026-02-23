package jp.kukv.portfolio.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
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
    val windowHeight = with(density) { windowInfo.containerSize.height.toDp() }
    val windowSizeClass =
        when {
            windowWidth < 600.dp -> WindowSizeClass.Mobile
            windowWidth <= 893.dp -> WindowSizeClass.Tablet
            else -> WindowSizeClass.Desktop
        }

    SideEffect {
        viewModel.updateWindowSize(windowSizeClass, windowHeight)
    }

    AppTheme(viewModel) {
        when {
            viewModel.windowSizeState.isMobile -> MobileLayout()
            else -> DesktopLayout()
        }
    }
}
