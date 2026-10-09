package jp.kukv.portfolio.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private const val STAGGER_MILLIS = 70L

/**
 * Bento の 1 マス。表示時に index に応じて時間差でフェードとスケールで現れ、
 * onClick がある場合は押下中に少し縮む。
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BentoTile(
    index: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * STAGGER_MILLIS)
        visible = true
    }
    val motionScheme = MaterialTheme.motionScheme
    val alpha by animateFloatAsState(if (visible) 1f else 0f, motionScheme.defaultEffectsSpec())
    val appearScale by animateFloatAsState(if (visible) 1f else 0.9f, motionScheme.defaultSpatialSpec())
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressScale by animateFloatAsState(if (pressed) 0.96f else 1f, motionScheme.fastSpatialSpec())

    val tileModifier =
        modifier.graphicsLayer {
            this.alpha = alpha
            scaleX = appearScale * pressScale
            scaleY = appearScale * pressScale
        }
    val colors =
        CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColorFor(containerColor),
        )
    val body: @Composable ColumnScope.() -> Unit = {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp), content = content)
    }

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = tileModifier,
            shape = MaterialTheme.shapes.extraLarge,
            colors = colors,
            interactionSource = interactionSource,
            content = body,
        )
    } else {
        Card(
            modifier = tileModifier,
            shape = MaterialTheme.shapes.extraLarge,
            colors = colors,
            content = body,
        )
    }
}
