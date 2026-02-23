package jp.kukv.portfolio.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import jp.kukv.portfolio.app.LocalAppViewModel

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
                    contentDescription = "Menu",
                )
            }
            Text(
                "Portfolio",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

@Composable
fun DesktopHeader(onNavigate: (String) -> Unit) {
    val appViewModel = LocalAppViewModel.current
    val isTablet = appViewModel.windowSizeState.isTablet

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
                "Portfolio",
                style = MaterialTheme.typography.titleLarge,
            )
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(onClick = { onNavigate("home") }) { Text("Home") }
                TextButton(onClick = { onNavigate("about") }) { Text("About") }
                TextButton(onClick = { onNavigate("showcase") }) { Text("Showcase") }
                TextButton(onClick = { onNavigate("contact") }) { Text("Contact") }
            }
            if (!isTablet) {
                Box(modifier = Modifier.widthIn(min = 48.dp), contentAlignment = Alignment.CenterEnd) {
                    IconToggleButton(
                        checked = appViewModel.isDarkTheme,
                        onCheckedChange = { appViewModel.isDarkTheme = it },
                    ) {
                        Icon(
                            imageVector = if (appViewModel.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                            contentDescription = "Toggle theme",
                        )
                    }
                }
            } else {
                IconToggleButton(
                    checked = appViewModel.isDarkTheme,
                    onCheckedChange = { appViewModel.isDarkTheme = it },
                ) {
                    Icon(
                        imageVector = if (appViewModel.isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle theme",
                    )
                }
            }
        }
    }
}
