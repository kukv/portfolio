package jp.kukv.portfolio.shared.theme

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DarkThemeTest {
    @Test
    fun savedDarkWins() {
        assertTrue(resolveDarkTheme(saved = "dark", systemPrefersDark = false))
    }

    @Test
    fun savedLightWins() {
        assertFalse(resolveDarkTheme(saved = "light", systemPrefersDark = true))
    }

    @Test
    fun followsSystemWhenNothingSaved() {
        assertTrue(resolveDarkTheme(saved = null, systemPrefersDark = true))
        assertFalse(resolveDarkTheme(saved = null, systemPrefersDark = false))
    }

    @Test
    fun followsSystemWhenSavedValueIsInvalid() {
        assertTrue(resolveDarkTheme(saved = "blue", systemPrefersDark = true))
    }

    @Test
    fun storageValueRoundTrips() {
        assertEquals("dark", themeStorageValue(isDark = true))
        assertEquals("light", themeStorageValue(isDark = false))
        assertTrue(resolveDarkTheme(themeStorageValue(isDark = true), systemPrefersDark = false))
        assertFalse(resolveDarkTheme(themeStorageValue(isDark = false), systemPrefersDark = true))
    }
}
