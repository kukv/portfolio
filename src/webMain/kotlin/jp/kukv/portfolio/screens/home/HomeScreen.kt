package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.Section
import jp.kukv.portfolio.shared.layout.currentLayoutSize

private val Gap = 16.dp

@Composable
fun HomeScreen(
    onNavigate: (Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.widthIn(max = 1100.dp).fillMaxWidth()) {
            when (currentLayoutSize()) {
                LayoutSize.Expanded -> TwoColumnBento(onNavigate, nameWeight = 1.4f, photoSize = 200.dp)
                LayoutSize.Medium -> TwoColumnBento(onNavigate, nameWeight = 1.25f, photoSize = 112.dp)
                LayoutSize.Compact -> CompactBento(onNavigate)
            }
        }
    }
}

/** 2 列。左に自己紹介のタイル、右に AI といま作っているものを縦に並べる。 */
@Composable
private fun TwoColumnBento(
    onNavigate: (Section) -> Unit,
    nameWeight: Float,
    photoSize: Dp,
) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(Gap),
        ) {
            NameTile(index = 0, photoSize = photoSize, modifier = Modifier.weight(nameWeight).fillMaxHeight())
            Column(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(Gap),
            ) {
                AiTile(index = 1, modifier = Modifier.fillMaxWidth().weight(1f))
                NowBuildingTile(index = 2, modifier = Modifier.fillMaxWidth().weight(1f))
            }
        }
        LinkRow(onNavigate)
    }
}

/**
 * Showcase と Contact へのリンクタイルの行。
 * IntrinsicSize.Min の行に入れると日本語のラベルが描画されなくなるため、高さはそろえない(どちらも 1 行)。
 */
@Composable
private fun LinkRow(onNavigate: (Section) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Gap),
    ) {
        ShowcaseTile(index = 3, onNavigate = onNavigate, modifier = Modifier.weight(1f))
        ContactTile(index = 4, onNavigate = onNavigate, modifier = Modifier.weight(1f))
    }
}

/** 1 列。 */
@Composable
private fun CompactBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        NameTile(index = 0, photoSize = 120.dp, modifier = Modifier.fillMaxWidth())
        AiTile(index = 1, modifier = Modifier.fillMaxWidth())
        NowBuildingTile(index = 2, modifier = Modifier.fillMaxWidth())
        ShowcaseTile(index = 3, onNavigate = onNavigate, modifier = Modifier.fillMaxWidth())
        ContactTile(index = 4, onNavigate = onNavigate, modifier = Modifier.fillMaxWidth())
    }
}
