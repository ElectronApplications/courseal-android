package online.courseal.courseal_android.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import online.courseal.courseal_android.ui.components.CoursealPasswordField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopCancel
import online.courseal.courseal_android.ui.components.TopConfirm
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.settings.SettingsPasswordUiError
import online.courseal.courseal_android.ui.viewmodels.settings.SettingsPasswordViewModel

@Composable
fun SettingsPasswordScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    settingsPasswordViewModel: SettingsPasswordViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val settingsPasswordUiState by settingsPasswordViewModel.uiState.collectAsState()

    if (settingsPasswordUiState.errorUnrecoverableState != null) {
        onUnrecoverable(settingsPasswordUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = settingsPasswordUiState.errorState != SettingsPasswordUiError.NONE,
        hideDialog = settingsPasswordViewModel::hideError,
        title = when (settingsPasswordUiState.errorState) {
            SettingsPasswordUiError.NONE -> ""
            SettingsPasswordUiError.PASSWORD_INVALID -> stringResource(R.string.incorrect_password)
            else -> stringResource(R.string.unknown_error)
        }
    )

    Column(
        modifier = modifier
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
                enabled = !settingsPasswordUiState.makingRequest,
                onClick = {
                    coroutineScope.launch {
                        settingsPasswordViewModel.confirm(onGoBack)
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    text = stringResource(R.string.change_password),
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )

                CoursealPasswordField(
                    modifier = Modifier
                        .padding(top = 20.dp),
                    value = settingsPasswordUiState.oldPassword,
                    onValueChange = settingsPasswordViewModel::updateOldPassword,
                    label = stringResource(R.string.old_password),
                    enabled = !settingsPasswordUiState.makingRequest
                )

                CoursealPasswordField(
                    modifier = Modifier
                        .padding(top = 10.dp),
                    value = settingsPasswordUiState.newPassword,
                    onValueChange = settingsPasswordViewModel::updateNewPassword,
                    label = stringResource(R.string.new_password),
                    enabled = !settingsPasswordUiState.makingRequest
                )
            }
        }
    }
}