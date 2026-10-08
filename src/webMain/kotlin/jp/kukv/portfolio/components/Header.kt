package jp.kukv.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.i18n.AppLanguage
import jp.kukv.portfolio.shared.layout.Section
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.header_title
import portfolio.generated.resources.menu
import portfolio.generated.resources.toggle_theme

@Composable
fun MobileHeader(onMenuOpen: () -> Unit) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            IconButton(
                onClick = onMenuOpen,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(Res.string.menu),
                )
            }
            Text(
                stringResource(Res.string.header_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
fun DesktopHeader(
    onNavigate: (Section) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    language: AppLanguage,
    onLanguageChange: (AppLanguage) -> Unit,
) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                stringResource(Res.string.header_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                Section.entries.forEach { section ->
                    TextButton(onClick = { onNavigate(section) }) { Text(stringResource(section.label)) }
                }
            }
            LanguageToggle(language = language, onLanguageChange = onLanguageChange)
            Spacer(modifier = Modifier.width(8.dp))
            ThemeToggle(isDarkTheme = isDarkTheme, onThemeChange = onThemeChange)
        }
    }
}

@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    IconToggleButton(
        checked = isDarkTheme,
        onCheckedChange = onThemeChange,
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
            contentDescription = stringResource(Res.string.toggle_theme),
        )
    }
}
