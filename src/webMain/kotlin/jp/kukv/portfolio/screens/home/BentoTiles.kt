package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.Section
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.home_ai_body
import portfolio.generated.resources.home_ai_title
import portfolio.generated.resources.home_cta_contact
import portfolio.generated.resources.home_cta_showcase
import portfolio.generated.resources.home_greeting
import portfolio.generated.resources.home_name
import portfolio.generated.resources.home_now_body
import portfolio.generated.resources.home_now_title
import portfolio.generated.resources.home_role

@Composable
fun NameTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier, containerColor = MaterialTheme.colorScheme.primary) {
        Spacer(modifier = Modifier.weight(1f))
        Text(stringResource(Res.string.home_greeting), style = MaterialTheme.typography.titleMedium)
        Text(
            stringResource(Res.string.home_name),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(Res.string.home_role), style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun PhotoTile(
    index: Int,
    imageModifier: Modifier,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ProfileImage(modifier = imageModifier)
        }
    }
}

@Composable
fun AiTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    MessageTile(
        index = index,
        icon = Icons.Default.AutoAwesome,
        title = Res.string.home_ai_title,
        body = Res.string.home_ai_body,
        modifier = modifier,
    )
}

@Composable
fun NowBuildingTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    MessageTile(
        index = index,
        icon = Icons.Default.Construction,
        title = Res.string.home_now_title,
        body = Res.string.home_now_body,
        modifier = modifier,
    )
}

@Composable
private fun MessageTile(
    index: Int,
    icon: ImageVector,
    title: StringResource,
    body: StringResource,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(12.dp))
        Text(stringResource(title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(body), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun StatusTile(
    index: Int,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier, containerColor = MaterialTheme.colorScheme.surfaceContainer) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
            StatusPill()
        }
    }
}

@Composable
fun ShowcaseTile(
    index: Int,
    onNavigate: (Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    LinkTile(
        index = index,
        label = Res.string.home_cta_showcase,
        onClick = { onNavigate(Section.Showcase) },
        containerColor = MaterialTheme.colorScheme.tertiary,
        modifier = modifier,
    )
}

@Composable
fun ContactTile(
    index: Int,
    onNavigate: (Section) -> Unit,
    modifier: Modifier = Modifier,
) {
    LinkTile(
        index = index,
        label = Res.string.home_cta_contact,
        onClick = { onNavigate(Section.Contact) },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    )
}

@Composable
private fun LinkTile(
    index: Int,
    label: StringResource,
    onClick: () -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    BentoTile(index = index, modifier = modifier, containerColor = containerColor, onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(stringResource(label), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}
