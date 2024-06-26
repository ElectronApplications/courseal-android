package online.courseal.courseal_android.ui.screens.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.components.AnimatedArrowDown
import online.courseal.courseal_android.ui.components.CoursealPrimaryLoadingButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.auth.WelcomeViewModel

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    onStart: (serverRegistrationEnabled: Boolean, serverId: Long) -> Unit,
    welcomeViewModel: WelcomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val welcomeUiState by welcomeViewModel.uiState.collectAsState()

    ErrorDialog(
        isVisible = welcomeUiState.showError,
        hideDialog = welcomeViewModel::hideDialog,
        title = stringResource(R.string.connection_failed),
        text = stringResource(R.string.connection_failed_detailed)
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
                text = stringResource(R.string.learn_skills),
                textAlign = TextAlign.Center
            )

            var advancedVisible by rememberSaveable { mutableStateOf(false) }

            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(all = 5.dp)
                    .clickable {
                        advancedVisible = !advancedVisible
                    }
                    .padding(all = 10.dp),
            ) {
                Text(
                    text = stringResource(R.string.connecting_to) + " "
                )

                Text(
                    modifier = Modifier.weight(1f, fill = false),
                    text = welcomeUiState.serverUrl,
                    textDecoration = TextDecoration.Underline,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                AnimatedArrowDown(isExpanded = advancedVisible)
            }

            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.85f),
                visible = advancedVisible
            ) {
                CoursealTextField(
                    value = welcomeUiState.providedUrl,
                    onValueChange = {
                        welcomeViewModel.updateUrl(it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    label = stringResource(R.string.server_url))
            }

            CoursealPrimaryLoadingButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 15.dp)
                    .fillMaxWidth(0.85f),
                text = stringResource(R.string.get_started),
                loading = welcomeUiState.makingRequest,
                onClick = {
                    coroutineScope.launch {
                        welcomeViewModel.start(onStart)
                    }
                }
            )
        }
    }
}