package jp.kukv.portfolio

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.profile

@Composable
fun HomeSection(
    onNavigate: (String) -> Unit,
    topPadding: Dp = 0.dp,
    modifier: Modifier = Modifier,
) {
    val appState = LocalAppState.current
    val isMobile = appState.windowSize.isMobile
    val isTablet = appState.windowSize.isTablet
    val sectionHeight = appState.windowSize.windowHeight - topPadding
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(sectionHeight)
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (isMobile) {
            Column(
                modifier = Modifier.widthIn(max = 1000.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            ) {
                Image(
                    painter = painterResource(Res.drawable.profile),
                    contentDescription = "Profile Image",
                    modifier =
                        Modifier
                            .size(180.dp)
                            .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "and I'm a Software enggner",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onNavigate("showcase") }) {
                        Text("Showcase")
                    }
                }
            }
        } else if (isTablet) {
            Column(
                modifier = Modifier.widthIn(max = 1000.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
            ) {
                Image(
                    painter = painterResource(Res.drawable.profile),
                    contentDescription = "Profile Image",
                    modifier =
                        Modifier
                            .size(180.dp)
                            .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "and I'm a Software enggner",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = { onNavigate("showcase") }) {
                        Text("Showcase")
                    }
                }
            }
        } else {
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
