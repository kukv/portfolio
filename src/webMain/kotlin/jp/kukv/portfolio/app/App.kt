package jp.kukv.portfolio.app

import androidx.compose.foundation.ScrollState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.kukv.portfolio.shared.layout.DesktopLayout
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.MobileLayout
import jp.kukv.portfolio.shared.layout.Section
import jp.kukv.portfolio.shared.layout.currentLayoutSize

@Composable
fun App() {
    val viewModel: AppViewModel = viewModel { AppViewModel() }

    val scrollState = remember { ScrollState(0) }
    val sectionPositions = remember { mutableStateMapOf<Section, Int>() }
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme(viewModel) {
        when (currentLayoutSize()) {
            LayoutSize.Compact -> MobileLayout(scrollState, sectionPositions, snackbarHostState)
            else -> DesktopLayout(scrollState, sectionPositions, snackbarHostState)
        }
    }
}
