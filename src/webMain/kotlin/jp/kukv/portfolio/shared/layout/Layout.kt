package jp.kukv.portfolio.shared.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import jp.kukv.portfolio.app.LocalAppViewModel
import jp.kukv.portfolio.components.DesktopHeader
import jp.kukv.portfolio.components.Footer
import jp.kukv.portfolio.components.MobileHeader
import jp.kukv.portfolio.components.NavigationDrawer
import jp.kukv.portfolio.screens.about.AboutSection
import jp.kukv.portfolio.screens.contact.ContactSection
import jp.kukv.portfolio.screens.home.HomeSection
import jp.kukv.portfolio.screens.showcase.ShowcaseSection
import kotlinx.coroutines.launch

@Composable
fun MobileLayout() {
    val appViewModel = LocalAppViewModel.current
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = appViewModel.drawerState,
        drawerContent = { NavigationDrawer() },
    ) {
        Scaffold(
            topBar = {
                MobileHeader(
                    onMenuOpen = { scope.launch { appViewModel.drawerState.open() } },
                )
            },
            snackbarHost = { SnackbarHost(appViewModel.snackbarHostState) },
        ) { padding -> MainContent(padding) }
    }
}

@Composable
fun DesktopLayout() {
    val appViewModel = LocalAppViewModel.current

    Scaffold(
        topBar = {
            DesktopHeader(onNavigate = appViewModel::navigate)
        },
        snackbarHost = { SnackbarHost(appViewModel.snackbarHostState) },
    ) { padding -> MainContent(padding) }
}

@Composable
private fun MainContent(padding: PaddingValues) {
    val appViewModel = LocalAppViewModel.current

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(appViewModel.scrollState),
    ) {
        HomeSection(
            onNavigate = appViewModel::navigate,
            topPadding = padding.calculateTopPadding(),
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    appViewModel.sectionPositions["home"] = maxOf(0, pos)
                },
        )
        AboutSection(
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    appViewModel.sectionPositions["about"] = maxOf(0, pos)
                },
        )
        ShowcaseSection(
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    appViewModel.sectionPositions["showcase"] = maxOf(0, pos)
                },
        )
        ContactSection(
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    appViewModel.sectionPositions["contact"] = maxOf(0, pos)
                },
        )
        Footer()
    }
}
