package online.courseal.courseal_android.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopCancel
import online.courseal.courseal_android.ui.components.TopConfirm
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.profile.ProfileViewModel
import online.courseal.courseal_android.ui.viewmodels.settings.SettingsUsernameUiError
import online.courseal.courseal_android.ui.viewmodels.settings.SettingsUsernameViewModel

@Composable
fun SettingsUsernameScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    profileViewModel: ProfileViewModel,
    settingsUsernameViewModel: SettingsUsernameViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsUsernameUiState by settingsUsernameViewModel.uiState.collectAsState()

    if (settingsUsernameUiState.errorUnrecoverableState != null) {
        onUnrecoverable(settingsUsernameUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = settingsUsernameUiState.errorState != SettingsUsernameUiError.NONE,
        hideDialog = settingsUsernameViewModel::hideError,
        title = when (settingsUsernameUiState.errorState) {
            SettingsUsernameUiError.NONE -> ""
            else -> stringResource(R.string.unknown_error)
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TopCancel(onClick = onGoBack)
            TopConfirm(
                enabled = !settingsUsernameUiState.makingRequest,
                onClick = {
                    coroutineScope.launch {
                        settingsUsernameViewModel.confirm(onGoBack, profileViewModel)
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.change_username),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )

            CoursealTextField(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally),
                value = settingsUsernameUiState.username,
                onValueChange = settingsUsernameViewModel::updateUsername,
                label = stringResource(R.string.username),
                enabled = !settingsUsernameUiState.makingRequest
            )
        }
    }
}