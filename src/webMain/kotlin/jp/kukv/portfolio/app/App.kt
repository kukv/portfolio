package jp.kukv.portfolio.app

import androidx.compose.runtime.Composable
import jp.kukv.portfolio.shared.layout.DesktopLayout
import jp.kukv.portfolio.shared.layout.MobileLayout

@Composable
fun App() {
    val appState = rememberAppState()

    AppTheme(appState) {
        when {
            appState.windowSize.isMobile -> MobileLayout()
            else -> DesktopLayout()
        }
    }
}
