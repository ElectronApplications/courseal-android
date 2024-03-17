package online.courseal.courseal_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun WelcomeScreen(modifier: Modifier = Modifier, canGoBack: Boolean, setStatusBarColor: (color: Int, isTextDark: Boolean) -> Unit) {
    val context = LocalContext.current

    setStatusBarColor(LocalCoursealPalette.current.welcomeGradientTop.toArgb(), !isSystemInDarkTheme())

    val siteUrl by rememberSaveable { mutableStateOf("https://courseal.online") }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            LocalCoursealPalette.current.welcomeGradientTop,
                            LocalCoursealPalette.current.welcomeGradientBottom
                        )
                    )
                )
        ) {
            if (canGoBack) {
                Row(
                    modifier = Modifier
                        .padding(top = 10.dp, start = 15.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = context.getString(R.string.go_back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 5.dp)
                            .align(Alignment.CenterVertically),
                        text = context.getString(R.string.go_back),
                        color = MaterialTheme.colorScheme.secondary
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
                color = MaterialTheme.colorScheme.primary
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

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 25.dp),
            style = MaterialTheme.typography.headlineMedium,
            text = context.getString(R.string.learn_skills),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(all = 5.dp),
        ) {
            Text(
                text = "Connecting to "
            )
            Text(
                text = siteUrl,
                textDecoration = TextDecoration.Underline
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = "Expand"
            )
        }

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 15.dp),
            onClick = { /*TODO*/ },
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 10.dp),
                text = context.getString(R.string.get_started)
            )
        }
    }
}