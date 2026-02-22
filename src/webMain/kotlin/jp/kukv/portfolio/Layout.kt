package jp.kukv.portfolio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import kotlinx.coroutines.launch

@Composable
fun MobileLayout() {
    val appState = LocalAppState.current

    ModalNavigationDrawer(
        drawerState = appState.navigation.drawerState,
        drawerContent = { NavigationDrawer() },
    ) {
        Scaffold(
            topBar = {
                MobileHeader(
                    onMenuOpen = { appState.navigation.scope.launch { appState.navigation.drawerState.open() } },
                )
            },
            snackbarHost = { SnackbarHost(appState.navigation.snackbarHostState) },
        ) { padding -> MainContent(padding) }
    }
}

@Composable
fun DesktopLayout() {
    val appState = LocalAppState.current

    Scaffold(
        topBar = {
            DesktopHeader(onNavigate = appState.navigation::navigate)
        },
        snackbarHost = { SnackbarHost(appState.navigation.snackbarHostState) },
    ) { padding -> MainContent(padding) }
}

@Composable
private fun MainContent(padding: PaddingValues) {
    val appState = LocalAppState.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .verticalScroll(appState.navigation.scrollState),
    ) {
        HomeSection(
            onNavigate = appState.navigation::navigate,
            topPadding = padding.calculateTopPadding(),
            modifier = Modifier.onGloballyPositioned { coordinates ->
                val pos = coordinates.positionInParent().y.toInt()
                appState.navigation.sectionPositions["home"] = maxOf(0, pos)
            },
        )
        AboutSection(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                val pos = coordinates.positionInParent().y.toInt()
                appState.navigation.sectionPositions["about"] = maxOf(0, pos)
            },
        )
        ShowcaseSection(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                val pos = coordinates.positionInParent().y.toInt()
                appState.navigation.sectionPositions["showcase"] = maxOf(0, pos)
            },
        )
        ContactSection(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                val pos = coordinates.positionInParent().y.toInt()
                appState.navigation.sectionPositions["contact"] = maxOf(0, pos)
            },
        )
        Footer()
    }
}
