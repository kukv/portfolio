package jp.kukv.portfolio.shared.layout

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

/** 画面幅の 3 区分。境界は Material の標準(600dp / 840dp)。 */
enum class LayoutSize {
    Compact,
    Medium,
    Expanded,
}

@Composable
fun currentLayoutSize(): LayoutSize {
    val sizeClass = currentWindowAdaptiveInfoV2().windowSizeClass
    return when {
        sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> LayoutSize.Expanded
        sizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> LayoutSize.Medium
        else -> LayoutSize.Compact
    }
}
