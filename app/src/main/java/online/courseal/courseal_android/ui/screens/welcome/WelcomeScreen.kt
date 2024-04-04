package online.courseal.courseal_android.ui.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.AuthViewModel

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    onStart: (serverRegistrationEnabled: Boolean) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val authUiState by authViewModel.uiState.collectAsState()

    var connectionFailedVisible by remember { mutableStateOf(false) }
    ErrorDialog(
        isVisible = connectionFailedVisible,
        setVisible = { connectionFailedVisible = it },
        title = context.getString(R.string.connection_failed),
        text = context.getString(R.string.connection_failed_detailed)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        WelcomeGradient(
            onGoBack = onGoBack
        )

        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
        ) {
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
            var userUrl by rememberSaveable { mutableStateOf("") }

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
                        .padding(all = 10.dp)
                ) {
                    Text(
                        text = context.getString(R.string.connecting_to) + " "
                    )

                    val displayUrl = if (authUiState.serverUrl.length > 24) {
                        authUiState.serverUrl.take(21) + "..."
                    } else authUiState.serverUrl

                    Text(
                        text = displayUrl,
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

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.85f),
                visible = advancedVisible
            ) {
                CoursealTextField(
                    value = userUrl,
                    onValueChange = {
                        userUrl = it
                        authViewModel.updateUrl(userUrl)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    label = context.getString(R.string.server_url))
            }

            var makingRequest by rememberSaveable { mutableStateOf(false) }
            CoursealPrimaryButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 15.dp)
                    .fillMaxWidth(0.85f),
                text = if (!makingRequest) context.getString(R.string.get_started) else context.getString(R.string.loading),
                enabled = !makingRequest,
                onClick = {
                    makingRequest = true
                    coroutineScope.launch {
                        if (authViewModel.getServerInfo()) {
                            onStart(authUiState.serverRegistrationEnabled)
                        } else {
                            connectionFailedVisible = true
                        }
                        makingRequest = false
                    }
                }
            )

            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
        }
    }
}