package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun Introduction(onNavigate: (String) -> Unit) {
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
