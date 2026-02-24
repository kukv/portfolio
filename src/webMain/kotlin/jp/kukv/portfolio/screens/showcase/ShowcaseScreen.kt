package jp.kukv.portfolio.screens.showcase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.kukv.portfolio.app.LocalAppViewModel

@Immutable
data class Project(
    val name: String,
    val technologies: List<String>,
    val description: String,
    val imageUrl: String,
    val url: String,
    val addedDate: String,
    val artifacts: List<String> = emptyList(),
)

// TODO: 実際のプロジェクトデータに置き換える（imageUrl・url を含む）
private val projects =
    listOf(
        Project(
            "E-Commerce Platform",
            listOf("Kotlin", "Compose", "Ktor"),
            "A full-stack e-commerce solution with real-time inventory management.",
            "",
            "https://example.com/shop",
            "2024-01-15",
        ),
        Project(
            "Weather Forecast App",
            listOf("Kotlin", "Multiplatform", "OpenWeather API"),
            "A multiplatform weather application providing hyper-local forecasts.",
            "",
            "https://example.com/weather",
            "2024-02-10",
        ),
        Project(
            "Task Management Tool",
            listOf("Kotlin", "Compose Desktop", "SQLite"),
            "A powerful desktop application for managing complex project tasks and timelines.",
            "",
            "https://example.com/tasks",
            "2024-03-05",
        ),
        Project(
            "Social Media Dashboard",
            listOf("Kotlin", "Compose Web", "GraphQL"),
            "A unified dashboard for tracking engagement across multiple social platforms.",
            "",
            "https://example.com/social",
            "2024-04-20",
        ),
        Project(
            "Fitness Tracker",
            listOf("Kotlin", "HealthConnect", "Compose"),
            "An Android app that integrates with wearables to track daily activity and health metrics.",
            "",
            "https://example.com/fitness",
            "2024-05-12",
        ),
        Project(
            "Budget Planner",
            listOf("Kotlin", "KMP", "Firebase"),
            "Cross-platform budget planning tool with cloud synchronization and data visualization.",
            "",
            "https://example.com/budget",
            "2024-06-18",
        ),
        Project(
            "Music Streaming Client",
            listOf("Kotlin", "ExoPlayer", "Compose"),
            "A sleek music player interface with support for high-quality audio streaming.",
            "",
            "https://example.com/music",
            "2024-07-22",
        ),
        Project(
            "Recipe Finder",
            listOf("Kotlin", "Room", "Compose"),
            "An offline-first application for discovering and storing personalized recipes.",
            "",
            "https://example.com/recipes",
            "2024-08-30",
        ),
        Project(
            "Crypto Wallet Tracker",
            listOf("Kotlin", "Web3j", "Compose"),
            "Securely track cryptocurrency portfolios and view real-time market trends.",
            "",
            "https://example.com/crypto",
            "2024-09-14",
        ),
        Project(
            "Code Snippet Manager",
            listOf("Kotlin", "Compose Desktop", "Highlight.js"),
            "A developer tool for organizing and searching frequently used code snippets.",
            "",
            "https://example.com/code",
            "2024-10-05",
        ),
        Project(
            "Travel Itinerary Planner",
            listOf("Kotlin", "Google Maps API", "KMP"),
            "Plan and share travel routes with integrated maps and booking features.",
            "",
            "https://example.com/travel",
            "2024-11-11",
        ),
        Project(
            "IoT Home Controller",
            listOf("Kotlin", "MQTT", "Compose"),
            "A centralized app for managing smart home devices via the MQTT protocol.",
            "",
            "https://example.com/iot",
            "2024-12-01",
        ),
        Project(
            "Language Learning App",
            listOf("Kotlin", "AI/ML", "Compose"),
            "Interactive language lessons powered by basic machine learning for pronunciation.",
            "",
            "https://example.com/learn",
            "2025-01-08",
        ),
        Project(
            "Personal Finance API",
            listOf("Kotlin", "Spring Boot", "PostgreSQL"),
            "A robust RESTful API for managing personal financial transactions and reports.",
            "",
            "https://example.com/api",
            "2025-01-25",
        ),
        Project(
            "Markdown Editor",
            listOf("Kotlin", "Compose Web", "Kotlinx.html"),
            "A browser-based markdown editor with real-time preview and export options.",
            "",
            "https://example.com/markdown",
            "2025-02-05",
        ),
        Project(
            "Event Booking System",
            listOf("Kotlin", "Compose", "Stripe API"),
            "End-to-end event management system with ticket booking and payment integration.",
            "",
            "https://example.com/events",
            "2025-02-14",
        ),
        Project(
            "Inventory Scanner",
            listOf("Kotlin", "ML Kit", "CameraX"),
            "Barcode and QR code scanner optimized for warehouse inventory management.",
            "",
            "https://example.com/scan",
            "2025-02-28",
        ),
        Project(
            "Chat Application",
            listOf("Kotlin", "WebSockets", "Compose"),
            "Real-time chat platform with support for group channels and file sharing.",
            "",
            "https://example.com/chat",
            "2025-03-10",
        ),
        Project(
            "News Aggregator",
            listOf("Kotlin", "RSS", "Compose"),
            "Aggregates news from various sources into a single, customizable feed.",
            "",
            "https://example.com/news",
            "2025-03-22",
        ),
        Project(
            "Plant Care Assistant",
            listOf("Kotlin", "TensorFlow Lite", "Compose"),
            "Identifies plants and provides care instructions based on photo analysis.",
            "",
            "https://example.com/plants",
            "2025-04-01",
        ),
    )

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseScreen(modifier: Modifier = Modifier) {
    val appViewModel = LocalAppViewModel.current
    val isMobile = appViewModel.windowSizeState.isMobile
    val isTablet = appViewModel.windowSizeState.isTablet
    val viewModel: ShowcaseViewModel = viewModel { ShowcaseViewModel() }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Showcase",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(32.dp))

        val itemsToShow =
            remember(viewModel.visibleCount) {
                val base: List<Project?> = projects.take(viewModel.visibleCount)
                base + List(maxOf(0, 8 - base.size)) { null }
            }

        FlowRow(
            modifier = Modifier.widthIn(max = 1300.dp),
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow =
                if (isMobile) {
                    1
                } else if (isTablet) {
                    2
                } else {
                    4
                },
        ) {
            itemsToShow.forEachIndexed { index, project ->
                key(project?.name ?: "coming-soon-$index") {
                    var isVisible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        isVisible = true
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter =
                            fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = (index % 8) * 100)) +
                                scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 500, delayMillis = (index % 8) * 100)),
                    ) {
                        ProjectCard(
                            project = project,
                            isMobile = isMobile,
                            isTablet = isTablet,
                            onClick = { viewModel.selectProject(it) },
                        )
                    }
                }
            }
        }

        if (projects.size > viewModel.visibleCount) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { viewModel.loadMore() }) {
                Text("More")
            }
        }
    }

    viewModel.selectedProject?.let { project ->
        ProjectDetailBottomSheet(
            project = project,
            onDismiss = { viewModel.dismissProject() },
        )
    }
}

@Composable
private fun ProjectCard(
    project: Project?,
    isMobile: Boolean,
    isTablet: Boolean,
    onClick: (Project) -> Unit,
) {
    val cardHeight =
        if (isMobile) {
            200.dp
        } else if (isTablet) {
            240.dp
        } else {
            280.dp
        }
    val cardModifier =
        if (isMobile) {
            Modifier.padding(8.dp).fillMaxWidth().height(cardHeight)
        } else {
            Modifier.padding(8.dp).size(width = 300.dp, height = cardHeight)
        }
    Card(
        modifier = cardModifier,
        onClick = { project?.let { onClick(it) } },
        enabled = project != null,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                if (project != null) {
                    // TODO: project.imageUrl を使用した実際の画像表示に置き換える
                    Text("Image Placeholder")
                } else {
                    Text("Coming Soon", style = MaterialTheme.typography.titleMedium)
                }
            }
            if (project != null) {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(text = project.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Added: ${project.addedDate}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectDetailBottomSheet(
    project: Project,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp),
        ) {
            Text(text = project.name, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Technologies: ${project.technologies.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Added: ${project.addedDate}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = project.description, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // TODO: クリッカブルなリンクに変更する（例: kotlinx.browser.window.open(project.url, "_blank")）
            Text(text = "URL: ${project.url}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Close")
            }
        }
    }
}
