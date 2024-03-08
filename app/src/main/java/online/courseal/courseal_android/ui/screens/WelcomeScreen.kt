package online.courseal.courseal_android.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R

@Composable
fun WelcomeScreen(canGoBack: Boolean) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            if (canGoBack) {
                Icon (
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Go back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text (
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                text = "Courseal",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.secondary
            )

            Image (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo"
            )

            Text (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(all = 25.dp),
                text = "Learn new skills by completing practice based lessons"
            )

            Button (
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                onClick = { /*TODO*/ },
            ) {
                Text (
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp),
                    text = "Get started"
                )
            }
        }
}