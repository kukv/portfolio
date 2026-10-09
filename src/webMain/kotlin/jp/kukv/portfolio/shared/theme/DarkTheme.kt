package jp.kukv.portfolio.shared.theme

private const val THEME_DARK = "dark"
private const val THEME_LIGHT = "light"

fun resolveDarkTheme(
    saved: String?,
    systemPrefersDark: Boolean,
): Boolean =
    when (saved) {
        THEME_DARK -> true
        THEME_LIGHT -> false
        else -> systemPrefersDark
    }

fun themeStorageValue(isDark: Boolean): String = if (isDark) THEME_DARK else THEME_LIGHT
