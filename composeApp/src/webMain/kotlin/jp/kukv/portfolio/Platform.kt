package jp.kukv.portfolio

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform