@file:OptIn(ExperimentalTime::class)

package jp.kukv.personal._extensions

import jp.kukv.portfolio._extensions.kotlinx.datetime.JST
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.JST): LocalDateTime {
    val instant = Clock.System.now()
    return instant.toLocalDateTime(timeZone)
}
