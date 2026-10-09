package jp.kukv.portfolio.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.app.LocalAppViewModel
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.currentLayoutSize
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.about_title
import portfolio.generated.resources.experience_title
import portfolio.generated.resources.skills_title

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    val language = LocalAppViewModel.current.language
    // 読み込み中と失敗時は null。そのあいだは各セクションの見出しだけを表示する。
    val content by produceState<AboutContent?>(initialValue = null, language) {
        value = loadAboutContentOrNull(language)
    }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AboutMeSection(content?.about)

        Spacer(modifier = Modifier.height(60.dp))

        SkillAndStacksSection(content?.skills.orEmpty())

        Spacer(modifier = Modifier.height(60.dp))

        ExperienceSection(content?.experiences.orEmpty())
    }
}

@Composable
fun AboutMeSection(about: String?) {
    Text(
        text = stringResource(Res.string.about_title),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    if (about == null) return
    Spacer(modifier = Modifier.height(24.dp))
    Surface(
        modifier = Modifier.widthIn(max = 800.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            about.split("\n\n").forEach { paragraph ->
                Text(text = paragraph, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun SkillAndStacksSection(skills: List<SkillCategory>) {
    val columns =
        when (currentLayoutSize()) {
            LayoutSize.Compact -> 1
            LayoutSize.Medium -> 2
            LayoutSize.Expanded -> 3
        }

    Text(
        text = stringResource(Res.string.skills_title),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Column(
        modifier = Modifier.widthIn(max = 1000.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        skills.chunked(columns).forEach { rowCategories ->
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                rowCategories.forEach { category ->
                    SkillCategoryCard(category = category, modifier = Modifier.weight(1f).fillMaxHeight())
                }
                repeat(columns - rowCategories.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ExperienceSection(experiences: List<Experience>) {
    Text(
        text = stringResource(Res.string.experience_title),
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Column(modifier = Modifier.widthIn(max = 800.dp).fillMaxWidth()) {
        experiences.forEachIndexed { index, exp ->
            Row(modifier = Modifier.height(IntrinsicSize.Max)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(24.dp).fillMaxHeight(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .padding(top = 6.dp)
                                .size(10.dp)
                                .background(MaterialTheme.colorScheme.primary, CircleShape),
                    )
                    if (index < experiences.size - 1) {
                        Box(
                            modifier =
                                Modifier
                                    .width(1.dp)
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        )
                    }
                }
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(
                                start = 16.dp,
                                bottom = if (index < experiences.size - 1) 32.dp else 0.dp,
                            ),
                ) {
                    Text(
                        text = exp.period,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exp.role,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = exp.organization,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exp.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                    if (exp.technologies.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = exp.technologies.joinToString(" · "),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                        )
                    }
                }
            }
        }
    }
}
