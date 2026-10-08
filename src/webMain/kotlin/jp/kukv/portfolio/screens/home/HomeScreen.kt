package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                LayoutSize.Expanded -> ExpandedBento(onNavigate)
                LayoutSize.Medium -> MediumBento(onNavigate)
                LayoutSize.Compact -> CompactBento(onNavigate)
            }
        }
    }
}

/** 3 列。名前タイルが左で縦 2 マスを占める。 */
@Composable
private fun ExpandedBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        BentoRow {
            NameTile(index = 0, modifier = Modifier.weight(1.2f).fillMaxHeight())
            Column(modifier = Modifier.weight(2f), verticalArrangement = Arrangement.spacedBy(Gap)) {
                BentoRow {
                    PhotoTile(
                        index = 1,
                        imageModifier = Modifier.fillMaxWidth().aspectRatio(1f),
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    )
                    AiTile(index = 2, modifier = Modifier.weight(1f).fillMaxHeight())
                }
                NowBuildingTile(index = 3, modifier = Modifier.fillMaxWidth())
            }
        }
        LinkRow(onNavigate)
    }
}

/** 2 列。 */
@Composable
private fun MediumBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        BentoRow {
            NameTile(index = 0, modifier = Modifier.weight(1f).fillMaxHeight())
            PhotoTile(
                index = 1,
                imageModifier = Modifier.fillMaxWidth().aspectRatio(1f),
                modifier = Modifier.weight(1f).fillMaxHeight(),
            )
        }
        BentoRow {
            AiTile(index = 2, modifier = Modifier.weight(1f).fillMaxHeight())
            NowBuildingTile(index = 3, modifier = Modifier.weight(1f).fillMaxHeight())
        }
        LinkRow(onNavigate)
    }
}

/**
 * Showcase と Contact へのリンクタイルの行。どちらも 1 行なので高さはそろえない。
 * IntrinsicSize.Min の行に入れると日本語のラベルが描画されなくなるため、BentoRow は使わない。
 */
@Composable
private fun LinkRow(onNavigate: (Section) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Gap),
    ) {
        ShowcaseTile(index = 4, onNavigate = onNavigate, modifier = Modifier.weight(1f))
        ContactTile(index = 5, onNavigate = onNavigate, modifier = Modifier.weight(1f))
    }
}

/** 1 列。名前 → 写真 → 残りの順。 */
@Composable
private fun CompactBento(onNavigate: (Section) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(Gap)) {
        NameTile(index = 0, modifier = Modifier.fillMaxWidth().height(260.dp))
        PhotoTile(index = 1, imageModifier = Modifier.size(200.dp), modifier = Modifier.fillMaxWidth())
        AiTile(index = 2, modifier = Modifier.fillMaxWidth())
        NowBuildingTile(index = 3, modifier = Modifier.fillMaxWidth())
        ShowcaseTile(index = 4, onNavigate = onNavigate, modifier = Modifier.fillMaxWidth())
        ContactTile(index = 5, onNavigate = onNavigate, modifier = Modifier.fillMaxWidth())
    }
}

/** 中のタイルの高さを、いちばん高いタイルにそろえる行。 */
@Composable
private fun BentoRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(Gap),
        content = content,
    )
}
