package jp.kukv.portfolio.shared.layout

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
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
    val scrollState = remember { ScrollState(0) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val snackbarHostState = remember { SnackbarHostState() }
    val sectionPositions: SnapshotStateMap<String, Int> = remember { emptyList<Pair<String, Int>>().toMutableStateMap() }
    val scope = rememberCoroutineScope()

    fun navigate(section: String) {
        scope.launch {
            val pos = sectionPositions[section] ?: 0
            scrollState.animateScrollTo(pos)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                onNavigate = { section ->
                    scope.launch { drawerState.close() }
                    navigate(section)
                },
                isDarkTheme = appViewModel.isDarkTheme,
                onThemeChange = { appViewModel.isDarkTheme = it },
            )
        },
    ) {
        Scaffold(
            topBar = {
                MobileHeader(
                    onMenuOpen = { scope.launch { drawerState.open() } },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            MainContent(
                padding = padding,
                scrollState = scrollState,
                sectionPositions = sectionPositions,
                snackbarHostState = snackbarHostState,
                onNavigate = ::navigate,
            )
        }
    }
}

@Composable
fun DesktopLayout() {
    val appViewModel = LocalAppViewModel.current
    val scrollState = remember { ScrollState(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val sectionPositions: SnapshotStateMap<String, Int> = remember { emptyList<Pair<String, Int>>().toMutableStateMap() }
    val scope = rememberCoroutineScope()

    fun navigate(section: String) {
        scope.launch {
            val pos = sectionPositions[section] ?: 0
            scrollState.animateScrollTo(pos)
        }
    }

    Scaffold(
        topBar = {
            DesktopHeader(
                onNavigate = ::navigate,
                isTablet = appViewModel.windowSizeState.isTablet,
                isDarkTheme = appViewModel.isDarkTheme,
                onThemeChange = { appViewModel.isDarkTheme = it },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        MainContent(
            padding = padding,
            scrollState = scrollState,
            sectionPositions = sectionPositions,
            snackbarHostState = snackbarHostState,
            onNavigate = ::navigate,
        )
    }
}

@Composable
private fun MainContent(
    padding: PaddingValues,
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<String, Int>,
    snackbarHostState: SnackbarHostState,
    onNavigate: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
    ) {
        HomeSection(
            onNavigate = onNavigate,
            topPadding = padding.calculateTopPadding(),
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    sectionPositions["home"] = maxOf(0, pos)
                },
        )
        AboutSection(
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    sectionPositions["about"] = maxOf(0, pos)
                },
        )
        ShowcaseSection(
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    sectionPositions["showcase"] = maxOf(0, pos)
                },
        )
        ContactSection(
            onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
            modifier =
                Modifier.onGloballyPositioned { coordinates ->
                    val pos = coordinates.positionInParent().y.toInt()
                    sectionPositions["contact"] = maxOf(0, pos)
                },
        )
        Footer()
    }
}
