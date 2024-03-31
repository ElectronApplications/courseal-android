package online.courseal.courseal_android.ui.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.SetStatusBarColor
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun WelcomeGradient(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    setStatusBarColor: SetStatusBarColor
) {
    val context = LocalContext.current
    setStatusBarColor(LocalCoursealPalette.current.welcomeGradientTop, !isSystemInDarkTheme())

    Column(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        LocalCoursealPalette.current.welcomeGradientTop,
                        LocalCoursealPalette.current.welcomeGradientBottom
                    )
                )
            )
    ) {
        if (onGoBack != null) {
            Row(
                modifier = Modifier
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

        Text(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            text = context.getString(R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            color = LocalCoursealPalette.current.onWelcomeGradient
        )

        val coursealVisibility = remember {
            MutableTransitionState(false).apply {
                targetState = true
            }
        }
        AnimatedVisibility(
            modifier = Modifier
                .width(150.dp)
                .align(alignment = Alignment.CenterHorizontally),
            visibleState = coursealVisibility,
            enter = slideInVertically(initialOffsetY = { it }, animationSpec = tween(1000))
        ) {
            Image(
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.courseal_not_rounded),
                contentDescription = context.getString(R.string.logo)
            )
        }
    }
}