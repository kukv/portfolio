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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio.shared.layout.LayoutSize
import jp.kukv.portfolio.shared.layout.currentLayoutSize
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.skills_title

data class SkillCategory(val label: String, val skills: List<String>)

data class Experience(
    val period: String,
    val role: String,
    val company: String,
    val description: String,
)

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AboutMeSection()

        Spacer(modifier = Modifier.height(60.dp))

        SkillAndStacksSection()

        Spacer(modifier = Modifier.height(60.dp))

        ExperienceSection()
    }
}

@Composable
fun AboutMeSection() {
    Text(
        text = "About Me",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(24.dp))
    Surface(
        modifier = Modifier.widthIn(max = 800.dp),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Text(
            text =
                "Hello! I am a software engineer specializing in Kotlin. I have experience in building multiplatform " +
                    "applications using Kotlin Multiplatform and Compose Multiplatform. I love creating efficient and maintainable code.",
            modifier = Modifier.padding(24.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

private val skillCategories =
    listOf(
        SkillCategory(
            "Languages / Core",
            listOf("Kotlin", "Java", "Swift", "TypeScript"),
        ),
        SkillCategory(
            "Multiplatform",
            listOf("Kotlin Multiplatform", "Compose Multiplatform", "Compose Android", "Compose Web", "Ktor"),
        ),
        SkillCategory(
            "Infra / Tools",
            listOf("Android Studio", "Gradle", "GitHub Actions", "Docker", "Firebase", "PostgreSQL"),
        ),
    )

@Composable
fun SkillAndStacksSection() {
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
        skillCategories.chunked(columns).forEach { rowCategories ->
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

private val experiences =
    listOf(
        Experience(
            period = "2022.04 — Present",
            role = "Senior Software Engineer",
            company = "Kotlin Corp",
            description =
                "Leading development of KMP-based cross-platform products. " +
                    "Improved build performance by 40% through Gradle modularization and introduced Compose Multiplatform for web deployment.",
        ),
        Experience(
            period = "2020.04 — 2022.03",
            role = "Software Engineer",
            company = "Mobile Solutions Inc.",
            description =
                "Developed Android apps with Kotlin and Jetpack Compose. " +
                    "Delivered 10+ client applications with high performance and maintainability standards.",
        ),
        Experience(
            period = "2018.04 — 2020.03",
            role = "Junior Engineer",
            company = "Digital Lab LLC",
            description =
                "Worked on native Android and server-side Kotlin projects. " +
                    "Gained experience with REST API design and Kotlin coroutines.",
        ),
    )

@Composable
fun ExperienceSection() {
    val scope = remember { experiences }

    Text(
        text = "Experience",
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground,
    )
    Spacer(modifier = Modifier.height(32.dp))
    Column(modifier = Modifier.widthIn(max = 800.dp).fillMaxWidth()) {
        scope.forEachIndexed { index, exp ->
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
                    if (index < scope.size - 1) {
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
                                bottom = if (index < scope.size - 1) 32.dp else 0.dp,
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
                        text = exp.company,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exp.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}
