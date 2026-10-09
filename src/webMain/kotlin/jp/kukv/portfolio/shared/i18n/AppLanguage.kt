package jp.kukv.portfolio.shared.i18n

enum class AppLanguage(val tag: String) {
    En("en"),
    Ja("ja"),
    ;

    companion object {
        fun fromTag(tag: String?): AppLanguage? {
            val primary = tag?.substringBefore('-')?.lowercase() ?: return null
            return entries.firstOrNull { it.tag == primary }
        }
    }
}

fun resolveLanguage(
    saved: String?,
    browserLanguage: String?,
): AppLanguage = AppLanguage.fromTag(saved) ?: AppLanguage.fromTag(browserLanguage) ?: AppLanguage.En
