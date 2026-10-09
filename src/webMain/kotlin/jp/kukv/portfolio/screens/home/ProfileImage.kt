package jp.kukv.portfolio.screens.home

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import portfolio.generated.resources.Res
import portfolio.generated.resources.profile
import portfolio.generated.resources.profile_image

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.profile),
        contentDescription = stringResource(Res.string.profile_image),
        modifier = modifier.clip(MaterialShapes.Cookie9Sided.toShape()),
        contentScale = ContentScale.Crop,
    )
}
