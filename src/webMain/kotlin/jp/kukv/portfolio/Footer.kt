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

data class SocialLink(
    val name: String,
    val url: String,
    val socialIcon: SocialLinkIcon,
)

data class SocialLinkIcon(val logo: DrawableResource, val size: Dp = 520.dp)

@Composable
fun SocialLinks(
    socialLinks: List<SocialLink> =
        listOf(
            SocialLink(
                "X",
                "https://x.com/kukv",
                SocialLinkIcon(
                    Res.drawable.x,
                ),
            ),
            SocialLink(
                "Facebook",
                "https://www.facebook.com/04x17",
                SocialLinkIcon(
                    Res.drawable.facebook,
                ),
            ),
            SocialLink(
                "Instagram",
                "https://www.instagram.com/kukv",
                SocialLinkIcon(
                    Res.drawable.instagram,
                ),
            ),
            SocialLink(
                "BlueSky",
                "https://bsky.app/profile/kukv.jp",
                SocialLinkIcon(
                    Res.drawable.bluesky,
                ),
            ),
            SocialLink(
                "GitHub",
                "https://github.com/kukv",
                SocialLinkIcon(
                    Res.drawable.github,
                ),
            ),
        ),
) {
    socialLinks.forEach { link ->

        val socialLinkIcon = link.socialIcon
        IconButton(onClick = { openUrl(link.url) }) {
            Icon(
                painter = painterResource(socialLinkIcon.logo),
                contentDescription = link.name,
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
