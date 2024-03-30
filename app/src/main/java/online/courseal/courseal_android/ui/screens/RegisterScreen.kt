package online.courseal.courseal_android.ui.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.SetStatusBarColor

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    setStatusBarColor: SetStatusBarColor
) {
    setStatusBarColor(MaterialTheme.colorScheme.background, !isSystemInDarkTheme())
    Text("Register")
}