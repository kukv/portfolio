package jp.kukv.portfolio.screens.about

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

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
