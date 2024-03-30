package online.courseal.courseal_android.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.SetStatusBarColor
import online.courseal.courseal_android.data.api.coursealInfo
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun CoursealGradient(
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

        Image(
            modifier = Modifier
                .width(150.dp)
                .align(alignment = Alignment.CenterHorizontally),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = R.drawable.courseal_not_rounded),
            contentDescription = context.getString(R.string.logo)
        )
    }
}

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    onStart: () -> Unit,
    setStatusBarColor: SetStatusBarColor
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CoursealGradient(
            onGoBack = onGoBack,
            setStatusBarColor = setStatusBarColor
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 25.dp),
            style = MaterialTheme.typography.displayMedium,
            text = context.getString(R.string.learn_skills),
            textAlign = TextAlign.Center
        )

        var advancedVisible by rememberSaveable { mutableStateOf(false) }
        val arrowRotation by animateFloatAsState(targetValue = if (advancedVisible) -180.0f else 0.0f, label = "arrow rotation")
        var siteUrl by rememberSaveable { mutableStateOf("https://courseal.online") }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 5.dp)
                .clickable {
                    advancedVisible = !advancedVisible
                },
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = context.getString(R.string.connecting_to) + " "
                )
                Text(
                    text = siteUrl,
                    textDecoration = TextDecoration.Underline
                )
                Icon(
                    modifier = Modifier
                        .rotate(arrowRotation),
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "Expand"
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            AnimatedVisibility(
                visible = advancedVisible
            ) {
                CoursealTextField(
                    value = siteUrl,
                    onValueChange = { siteUrl = it },
                    label = context.getString(R.string.server_url))
            }
        }

        var startButtonEnabled by rememberSaveable { mutableStateOf(true) }
        CoursealPrimaryButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp, start = 25.dp, end = 25.dp)
                .fillMaxWidth(),
            text = if (startButtonEnabled) context.getString(R.string.get_started) else context.getString(R.string.loading),
            enabled = startButtonEnabled,
            onClick = {
                startButtonEnabled = false
                coroutineScope.launch {
                    coursealInfo(
                        url = siteUrl,
                        onError = { _, _ ->
                            startButtonEnabled = true
                            Toast.makeText(context, context.getString(R.string.connection_failed), Toast.LENGTH_SHORT).show()
                        },
                        onSuccess = {
                            startButtonEnabled = true
                            onStart()
                        }
                    )
                }
            }
        )
    }
}