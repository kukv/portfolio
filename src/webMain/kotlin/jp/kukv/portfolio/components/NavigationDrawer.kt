package jp.kukv.portfolio.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawer(
    onNavigate: (String) -> Unit,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxHeight().width(280.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
        ) {
            Text(
                "Portfolio",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            NavigationDrawerItem(
                label = { Text("Home") },
                selected = false,
                onClick = { onNavigate("home") },
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            NavigationDrawerItem(
                label = { Text("About") },
                selected = false,
                onClick = { onNavigate("about") },
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            NavigationDrawerItem(
                label = { Text("Showcase") },
                selected = false,
                onClick = { onNavigate("showcase") },
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            NavigationDrawerItem(
                label = { Text("Contact") },
                selected = false,
                onClick = { onNavigate("contact") },
                modifier = Modifier.padding(horizontal = 8.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                IconToggleButton(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange,
                ) {
                    Icon(
                        imageVector =
                            if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle theme",
                    )
                }
            }
        }
    }
}
