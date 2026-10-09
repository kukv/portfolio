package jp.kukv.portfolio.screens.about

import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.lib.logError
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import portfolio.generated.resources.Res
import kotlin.coroutines.cancellation.CancellationException

/** About セクションの中身。files/content/{言語}.json に置く。 */
@Serializable
data class AboutContent(
    val about: String,
    val skills: List<SkillCategory>,
    val experiences: List<Experience>,
)

@Serializable
data class SkillCategory(val label: String, val items: List<String>)

@Serializable
data class Experience(
    val period: String,
    val role: String,
    // 社名は出さず、業界と規模を書く。
    val organization: String,
    val description: String,
    val technologies: List<String>,
)

fun decodeAboutContent(text: String): AboutContent = Json.decodeFromString(text)

/** 表示中の言語の JSON を読む。取得や解析に失敗したら原因をコンソールに出して null を返す。 */
@OptIn(ExperimentalResourceApi::class)
suspend fun loadAboutContentOrNull(language: AppLanguage): AboutContent? {
    val path = "files/content/${language.tag}.json"
    return try {
        decodeAboutContent(Res.readBytes(path).decodeToString())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logError("$path を読み込めませんでした: ${e.message}")
        null
    }
}
