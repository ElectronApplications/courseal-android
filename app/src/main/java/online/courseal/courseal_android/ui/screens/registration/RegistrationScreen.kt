package online.courseal.courseal_android.ui.screens.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import online.courseal.courseal_android.ui.viewmodels.AuthViewModel

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val authUiState by authViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Text(authUiState.serverName)
    }
}