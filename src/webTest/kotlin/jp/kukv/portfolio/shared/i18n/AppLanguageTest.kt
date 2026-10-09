package jp.kukv.portfolio.shared.i18n

import kotlin.test.Test
import kotlin.test.assertEquals

class AppLanguageTest {
    @Test
    fun savedValueTakesPriorityOverBrowserLanguage() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = "ja", browserLanguage = "en-US"))
    }

    @Test
    fun japaneseBrowserResolvesToJapanese() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = null, browserLanguage = "ja-JP"))
    }

    @Test
    fun englishBrowserResolvesToEnglish() {
        assertEquals(AppLanguage.En, resolveLanguage(saved = null, browserLanguage = "en-US"))
    }

    @Test
    fun unsupportedBrowserLanguageFallsBackToEnglish() {
        assertEquals(AppLanguage.En, resolveLanguage(saved = null, browserLanguage = "fr-FR"))
    }

    @Test
    fun invalidSavedValueIsIgnored() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = "xx", browserLanguage = "ja"))
    }

    @Test
    fun missingValuesFallBackToEnglish() {
        assertEquals(AppLanguage.En, resolveLanguage(saved = null, browserLanguage = null))
    }

    @Test
    fun tagMatchingIgnoresCase() {
        assertEquals(AppLanguage.Ja, resolveLanguage(saved = null, browserLanguage = "JA-jp"))
    }
}
