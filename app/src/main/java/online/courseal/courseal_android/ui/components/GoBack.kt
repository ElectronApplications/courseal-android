package online.courseal.courseal_android.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun GoBack(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .padding(top = 10.dp, start = 15.dp)
            .clickable { onGoBack() }
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically),
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = context.getString(R.string.go_back),
            tint = LocalCoursealPalette.current.onWelcomeGradient
        )
        Text(
            modifier = Modifier
                .padding(start = 5.dp)
                .align(Alignment.CenterVertically),
            text = context.getString(R.string.go_back),
            color = LocalCoursealPalette.current.onWelcomeGradient
        )
    }
}