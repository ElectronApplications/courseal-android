package online.courseal.courseal_android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun TopButtonIconLeft(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 10.dp, start = 15.dp)
            .let { if (enabled) it.clickable(onClick = onClick) else it }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            imageVector = icon,
            contentDescription = text,
            tint = if(enabled) LocalCoursealPalette.current.topButtonTextColor else LocalCoursealPalette.current.topButtonTextColorDisabled
        )
        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterVertically),
            text = text,
            color = if(enabled) LocalCoursealPalette.current.topButtonTextColor else LocalCoursealPalette.current.topButtonTextColorDisabled
        )
    }
}

@Composable
fun TopButtonIconRight(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(top = 10.dp, end = 15.dp)
            .let { if (enabled) it.clickable(onClick = onClick) else it }
    ) {
        Text(
            modifier = Modifier
                .padding(end = 5.dp)
                .align(Alignment.CenterVertically),
            text = text,
            color = if(enabled) LocalCoursealPalette.current.topButtonTextColor else LocalCoursealPalette.current.topButtonTextColorDisabled
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            imageVector = icon,
            contentDescription = text,
            tint = if(enabled) LocalCoursealPalette.current.topButtonTextColor else LocalCoursealPalette.current.topButtonTextColorDisabled
        )
    }
}

@Composable
fun TopBack(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.go_back),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TopButtonIconLeft(
        modifier = modifier,
        text = text,
        icon = Icons.AutoMirrored.Outlined.ArrowBack,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun TopCancel(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.cancel),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TopButtonIconLeft(
        modifier = modifier,
        text = text,
        icon = Icons.Filled.Close,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun TopConfirm(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.confirm),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    TopButtonIconRight(
        modifier = modifier,
        text = text,
        icon = Icons.Filled.Check,
        enabled = enabled,
        onClick = onClick
    )
}