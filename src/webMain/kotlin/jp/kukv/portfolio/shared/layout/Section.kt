package jp.kukv.portfolio.shared.layout

import org.jetbrains.compose.resources.StringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.nav_about
import portfolio.generated.resources.nav_contact
import portfolio.generated.resources.nav_home
import portfolio.generated.resources.nav_showcase

enum class Section(val label: StringResource) {
    Home(Res.string.nav_home),
    About(Res.string.nav_about),
    Showcase(Res.string.nav_showcase),
    Contact(Res.string.nav_contact),
}
