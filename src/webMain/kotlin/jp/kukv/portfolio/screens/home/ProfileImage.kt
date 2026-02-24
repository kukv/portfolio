package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import org.jetbrains.compose.resources.painterResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.profile

@Composable
fun ProfileImage(size: Dp) {
    Image(
        painter = painterResource(Res.drawable.profile),
        contentDescription = "Profile Image",
        modifier =
            Modifier
                .size(size)
                .clip(CircleShape),
        contentScale = ContentScale.Crop,
    )
}
