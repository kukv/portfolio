package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.app.LocalAppViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit,
    topPadding: Dp = 0.dp,
) {
    val appViewModel = LocalAppViewModel.current
    val windowSizeState = appViewModel.windowSizeState

    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val windowHeight = with(density) { windowInfo.containerSize.height.toDp() }
    val sectionHeight = windowHeight - topPadding
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(sectionHeight)
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            windowSizeState.isDesktop -> DesktopIntroduction(onNavigate)
            else -> MobileTabletIntroduction(onNavigate)
        }
    }
}

@Composable
fun DesktopIntroduction(onNavigate: (String) -> Unit) {
    Row(
        modifier = Modifier.widthIn(max = 1000.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterHorizontally),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            StatusPill()
            Spacer(modifier = Modifier.height(16.dp))
            Introduction(onNavigate)
        }

        ProfileImage(300.dp)
    }
}

@Composable
fun MobileTabletIntroduction(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier.widthIn(max = 1000.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        ProfileImage(180.dp)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusPill()
            Spacer(modifier = Modifier.height(16.dp))
            Introduction(onNavigate)
        }
    }
}
