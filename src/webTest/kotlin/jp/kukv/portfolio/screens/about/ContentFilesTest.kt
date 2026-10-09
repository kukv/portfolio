package jp.kukv.portfolio.screens.about

import jp.kukv.portfolio.shared.i18n.AppLanguage
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/** files/content/ の実ファイルを確かめる。contentFixtures は build.gradle.kts の generateContentFixtures が生成する。 */
class ContentFilesTest {
    @Test
    fun everyLanguageHasContentFile() {
        assertEquals(AppLanguage.entries.map { it.tag }.toSet(), contentFixtures.keys)
    }

    @Test
    fun everyContentFileDecodes() {
        for ((name, text) in contentFixtures) {
            val result = runCatching { decodeAboutContent(text) }
            assertTrue(result.isSuccess, "$name.json を解析できません: ${result.exceptionOrNull()?.message}")
        }
    }
}
