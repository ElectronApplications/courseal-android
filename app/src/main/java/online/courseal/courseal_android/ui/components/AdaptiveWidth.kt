package online.courseal.courseal_android.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.adaptiveContainerWidth(maxWidth: Dp = 500.dp): Modifier {
    val width = LocalConfiguration.current.screenWidthDp
    val widthModifier = if (width > maxWidth.value)
        Modifier.width(maxWidth)
    else
        Modifier.fillMaxWidth()
    return this then widthModifier
}