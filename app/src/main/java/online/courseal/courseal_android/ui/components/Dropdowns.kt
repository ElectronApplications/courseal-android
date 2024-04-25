package online.courseal.courseal_android.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R

@Composable
fun AnimatedArrowDown(
    modifier: Modifier = Modifier,
    isExpanded: Boolean
) {
    val arrowRotation by animateFloatAsState(targetValue = if (isExpanded) -180.0f else 0.0f, label = "arrow rotation")
    Icon(
        modifier = modifier
            .rotate(arrowRotation),
        imageVector = Icons.Outlined.KeyboardArrowDown,
        contentDescription = stringResource(R.string.expand)
    )
}

@Composable
fun ColumnScope.CoursealDropdownMenu(
    modifier: Modifier = Modifier,
    visible: Boolean,
    setVisible: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Box {
        this@CoursealDropdownMenu.AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { setVisible(false) },
                color = Color.Black.copy(alpha = 0.5f),
                content = {}
            )
        }

        this@CoursealDropdownMenu.AnimatedVisibility(
            visible = visible
        ) {
            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = modifier.padding(8.dp),
                        content = content
                    )
                }
            }
        }
    }
}