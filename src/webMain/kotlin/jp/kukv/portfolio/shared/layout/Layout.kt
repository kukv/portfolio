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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import jp.kukv.portfolio.app.LocalAppViewModel
import jp.kukv.portfolio.components.DesktopHeader
import jp.kukv.portfolio.components.Footer
import jp.kukv.portfolio.components.MobileHeader
import jp.kukv.portfolio.components.NavigationDrawer
import jp.kukv.portfolio.screens.about.AboutScreen
import jp.kukv.portfolio.screens.contact.ContactScreen
import jp.kukv.portfolio.screens.home.HomeScreen
import jp.kukv.portfolio.screens.showcase.ShowcaseScreen
import kotlinx.coroutines.launch

@Composable
fun MobileLayout(
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
    snackbarHostState: SnackbarHostState,
) {
    val appViewModel = LocalAppViewModel.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    fun navigate(section: Section) {
        scope.launch { scrollToSection(section, scrollState, sectionPositions) }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            NavigationDrawer(
                onNavigate = { section ->
                    scope.launch {
                        drawerState.close()
                        scrollToSection(section, scrollState, sectionPositions)
                    }
                },
                isDarkTheme = appViewModel.isDarkTheme,
                onThemeChange = { appViewModel.setDarkTheme(it) },
                language = appViewModel.language,
                onLanguageChange = { appViewModel.setLanguage(it) },
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
fun DesktopLayout(
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
    snackbarHostState: SnackbarHostState,
) {
    val appViewModel = LocalAppViewModel.current
    val scope = rememberCoroutineScope()

    fun navigate(section: Section) {
        scope.launch { scrollToSection(section, scrollState, sectionPositions) }
    }

    Scaffold(
        topBar = {
            DesktopHeader(
                onNavigate = ::navigate,
                isDarkTheme = appViewModel.isDarkTheme,
                onThemeChange = { appViewModel.setDarkTheme(it) },
                language = appViewModel.language,
                onLanguageChange = { appViewModel.setLanguage(it) },
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

private suspend fun scrollToSection(
    section: Section,
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
) {
    val pos = sectionPositions[section] ?: 0
    scrollState.animateScrollTo(pos)
}

private fun Modifier.trackPosition(
    section: Section,
    sectionPositions: SnapshotStateMap<Section, Int>,
): Modifier =
    onGloballyPositioned { coordinates ->
        sectionPositions[section] = maxOf(0, coordinates.positionInParent().y.toInt())
    }

@Composable
private fun MainContent(
    padding: PaddingValues,
    scrollState: ScrollState,
    sectionPositions: SnapshotStateMap<Section, Int>,
    snackbarHostState: SnackbarHostState,
    onNavigate: (Section) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState),
    ) {
        HomeScreen(
            modifier = Modifier.trackPosition(Section.Home, sectionPositions),
            onNavigate = onNavigate,
            topPadding = padding.calculateTopPadding(),
        )
        AboutScreen(modifier = Modifier.trackPosition(Section.About, sectionPositions))
        ShowcaseScreen(modifier = Modifier.trackPosition(Section.Showcase, sectionPositions))
        ContactScreen(
            onShowSnackbar = { message -> snackbarHostState.showSnackbar(message) },
            modifier = Modifier.trackPosition(Section.Contact, sectionPositions),
        )
        Footer()
    }
}
