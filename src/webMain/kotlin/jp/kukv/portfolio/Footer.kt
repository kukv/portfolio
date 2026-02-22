package jp.kukv.portfolio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio._extensions.kotlinx.datetime.now
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.bluesky
import portfolio.generated.resources.facebook
import portfolio.generated.resources.github
import portfolio.generated.resources.instagram
import portfolio.generated.resources.x

@Composable
fun Footer() {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SocialLinks()
            }
            Spacer(modifier = Modifier.height(16.dp))
            CopyRights()
        }
    }
}

private enum class SocialLink(
    val serviceName: String,
    val url: String,
    val socialIcon: SocialLinkIcon,
) {
    X("X", "https://x.com/kukv", SocialLinkIcon.X),
    Facebook("Facebook", "https://www.facebook.com/04x17", SocialLinkIcon.Facebook),
    Instagram("Instagram", "https://www.instagram.com/kukv", SocialLinkIcon.Instagram),
    Bluesky("BlueSky", "https://bsky.app/profile/kukv.jp", SocialLinkIcon.Bluesky),
    Github("GitHub", "https://github.com/kukv", SocialLinkIcon.Github),
}

private enum class SocialLinkIcon(val logo: DrawableResource, val size: Dp = 520.dp) {
    X(Res.drawable.x),
    Facebook(Res.drawable.facebook),
    Instagram(Res.drawable.instagram),
    Bluesky(Res.drawable.bluesky),
    Github(Res.drawable.github),
}

private val socialLinks =
    listOf(
        SocialLink.X,
        SocialLink.Facebook,
        SocialLink.Instagram,
        SocialLink.Bluesky,
        SocialLink.Github,
    )

@Composable
fun SocialLinks() {
    socialLinks.forEach { link ->
        val socialLinkIcon = link.socialIcon
        IconButton(onClick = { openUrl(link.url) }) {
            Icon(
                painter = painterResource(socialLinkIcon.logo),
                contentDescription = link.serviceName,
                modifier = Modifier.size(socialLinkIcon.size),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
fun CopyRights() {
    val now = LocalDateTime.now()
    val year = now.year

    val author = "kukv(Nonaka koki)"

    Text(
        text = "© $year $author",
        style = MaterialTheme.typography.labelMedium,
    )
}
