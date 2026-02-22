package jp.kukv.portfolio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.kukv.portfolio._extensions.kotlinx.datetime.now
import jp.kukv.portfolio.ui.styles.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.bluesky
import portfolio.generated.resources.facebook
import portfolio.generated.resources.github
import portfolio.generated.resources.instagram
import portfolio.generated.resources.profile
import portfolio.generated.resources.x

@Composable
fun App() {
    var isDarkTheme by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val sectionPositions = remember { mutableStateMapOf<String, Int>() }
    val snackbarHostState = remember { SnackbarHostState() }

    AppTheme(isDarkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                Header(
                    isDarkTheme = isDarkTheme,
                    onThemeChange = { isDarkTheme = it },
                    onNavigate = { section ->
                        scope.launch {
                            val pos = sectionPositions[section] ?: 0
                            scrollState.animateScrollTo(pos)
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
            val density = LocalDensity.current
            val topPaddingPx = with(density) { padding.calculateTopPadding().toPx() }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(scrollState),
            ) {
                HomeSection(
                    onNavigate = { section ->
                        scope.launch {
                            val pos = sectionPositions[section] ?: 0
                            scrollState.animateScrollTo(pos)
                        }
                    },
                    topPadding = padding.calculateTopPadding(),
                    modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            val pos = (coordinates.positionInRoot().y + scrollState.value - topPaddingPx).toInt()
                            sectionPositions["home"] = maxOf(0, pos)
                        },
                )
                AboutSection(
                    modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            val pos = (coordinates.positionInRoot().y + scrollState.value - topPaddingPx).toInt()
                            sectionPositions["about"] = maxOf(0, pos)
                        },
                )
                ShowcaseSection(
                    modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            val pos = (coordinates.positionInRoot().y + scrollState.value - topPaddingPx).toInt()
                            sectionPositions["showcase"] = maxOf(0, pos)
                        },
                )
                ContactSection(
                    snackbarHostState = snackbarHostState,
                    modifier =
                        Modifier.onGloballyPositioned { coordinates ->
                            val pos = (coordinates.positionInRoot().y + scrollState.value - topPaddingPx).toInt()
                            sectionPositions["contact"] = maxOf(0, pos)
                        },
                )
                Footer()
            }
        }
    }
}

@Composable
fun HomeSection(
    onNavigate: (String) -> Unit,
    topPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
) {
    val windowInfo = LocalWindowInfo.current
    val windowHeight = with(LocalDensity.current) { windowInfo.containerSize.height.toDp() }
    val sectionHeight = windowHeight - topPadding
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(sectionHeight)
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.widthIn(max = 1000.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterHorizontally),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                StatusPill()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text =
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                                append("Hi, I'm ")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("Nonaka koki")
                            }
                        },
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "and I'm a Software enggner",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { onNavigate("showcase") }) {
                    Text("Showcase")
                }
            }

            Image(
                painter = painterResource(Res.drawable.profile),
                contentDescription = "Profile Image",
                modifier =
                    Modifier
                        .size(300.dp)
                        .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AboutSection(modifier: Modifier = Modifier) {
    val skillCategories =
        remember {
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
        }

    val experiences =
        remember {
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
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // About Me
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

        Spacer(modifier = Modifier.height(60.dp))

        // Skills & Stack
        Text(
            text = "Skills & Stack",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.widthIn(max = 1000.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            skillCategories.forEach { category ->
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = category.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            category.skills.forEach { skill ->
                                Surface(
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.surface,
                                ) {
                                    Text(
                                        text = skill,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        // Experience
        Text(
            text = "Experience",
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
}

@Composable
fun StatusPill() {
    val infiniteTransition = rememberInfiniteTransition(label = "statusPulse")
    val dotAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(durationMillis = 1000),
                repeatMode = RepeatMode.Reverse,
            ),
        label = "dotAlpha",
    )
    Row(
        modifier =
            Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    shape = CircleShape,
                ).padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(6.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = dotAlpha),
                        shape = CircleShape,
                    ),
        )
        Text(
            text = "Available for new opportunities",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

data class SkillCategory(val label: String, val skills: List<String>)

data class Project(
    val name: String,
    val technologies: List<String>,
    val description: String,
    val imageUrl: String,
    val url: String,
    val addedDate: String,
    val artifacts: List<String> = emptyList(),
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShowcaseSection(modifier: Modifier = Modifier) {
    val projects =
        remember {
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
        }

    var visibleCount by remember { mutableStateOf(8) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }
    val sheetState = rememberModalBottomSheetState()

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

        val itemsToShow = mutableListOf<Project?>()
        itemsToShow.addAll(projects.take(visibleCount))
        while (itemsToShow.size < 8) {
            itemsToShow.add(null) // Representing "Coming Soon"
        }

        FlowRow(
            modifier = Modifier.widthIn(max = 1300.dp), // Increased slightly to accommodate wider cards
            horizontalArrangement = Arrangement.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            maxItemsInEachRow = 4,
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
                            onClick = { selectedProject = it },
                        )
                    }
                }
            }
        }

        if (projects.size > visibleCount) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { visibleCount += 8 }) {
                Text("More")
            }
        }
    }

    if (selectedProject != null) {
        ProjectDetailBottomSheet(
            project = selectedProject!!,
            sheetState = sheetState,
            onDismiss = { selectedProject = null },
        )
    }
}

@Composable
fun ProjectCard(
    project: Project?,
    onClick: (Project) -> Unit,
) {
    Card(
        modifier =
            Modifier
                .padding(8.dp)
                .size(width = 300.dp, height = 280.dp),
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
fun ProjectDetailBottomSheet(
    project: Project,
    sheetState: androidx.compose.material3.SheetState,
    onDismiss: () -> Unit,
) {
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
            Text(text = "URL: ${project.url}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Close")
            }
        }
    }
}

data class Experience(
    val period: String,
    val role: String,
    val company: String,
    val description: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactSection(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var agreedToPrivacyPolicy by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val emailRegex =
        Regex(
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$",
        )

    val isFirstNameValid = firstName.isNotBlank() && firstName.length <= 100
    val isLastNameValid = lastName.isNotBlank() && lastName.length <= 100
    val isCompanyValid = company.isNotBlank() && company.length <= 100
    val isEmailValid = email.isNotBlank() && email.length <= 254 && emailRegex.matches(email)
    val isMessageValid = message.isNotBlank() && message.length <= 500

    val isFormValid =
        !isLoading &&
            isFirstNameValid &&
            isLastNameValid &&
            isCompanyValid &&
            isEmailValid &&
            isMessageValid &&
            agreedToPrivacyPolicy

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 60.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "contact us",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Please fill out the form below to get in touch with us.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Surface(
            modifier = Modifier.widthIn(max = 600.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First name*") },
                        modifier = Modifier.weight(1f),
                        isError = firstName.isNotEmpty() && !isFirstNameValid,
                        supportingText = {
                            if (firstName.isNotEmpty() && !isFirstNameValid) {
                                Text(
                                    if (firstName.isBlank()) "Required" else "Max 100 characters",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last name*") },
                        modifier = Modifier.weight(1f),
                        isError = lastName.isNotEmpty() && !isLastNameValid,
                        supportingText = {
                            if (lastName.isNotEmpty() && !isLastNameValid) {
                                Text(
                                    if (lastName.isBlank()) "Required" else "Max 100 characters",
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        },
                    )
                }

                OutlinedTextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("Company*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = company.isNotEmpty() && !isCompanyValid,
                    supportingText = {
                        if (company.isNotEmpty() && !isCompanyValid) {
                            Text(
                                if (company.isBlank()) "Required" else "Max 100 characters",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email*") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = email.isNotEmpty() && !isEmailValid,
                    supportingText = {
                        if (email.isNotEmpty() && !isEmailValid) {
                            val errorText =
                                when {
                                    email.isBlank() -> "Required"
                                    email.length > 254 -> "Max 254 characters"
                                    !emailRegex.matches(email) -> "Invalid email format"
                                    else -> ""
                                }
                            Text(errorText, color = MaterialTheme.colorScheme.error)
                        }
                    },
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("message*") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    isError = message.isNotEmpty() && !isMessageValid,
                    supportingText = {
                        if (message.isNotEmpty() && !isMessageValid) {
                            Text(
                                if (message.isBlank()) "Required" else "Max 500 characters",
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    },
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Switch(
                        checked = agreedToPrivacyPolicy,
                        onCheckedChange = { agreedToPrivacyPolicy = it },
                    )
                    Text(
                        text = "By selecting this, you agree to our privacy policy.",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }

                Button(
                    onClick = {
                        scope.launch {
                            isLoading = true
                            delay(2000)
                            isLoading = false
                            snackbarHostState.showSnackbar("Message sent successfully!")
                            // Optional: Reset form
                            firstName = ""
                            lastName = ""
                            company = ""
                            email = ""
                            message = ""
                            agreedToPrivacyPolicy = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isFormValid,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

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

@Composable
fun Header(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onNavigate: (String) -> Unit,
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
                "Portfolio",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f),
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

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                IconToggleButton(
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange,
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = "Toggle theme",
                    )
                }
            }
        }
    }
}
