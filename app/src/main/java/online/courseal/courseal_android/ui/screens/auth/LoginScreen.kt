package online.courseal.courseal_android.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealPasswordField
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealPrimaryLoadingButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.GoBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.LoginUiError
import online.courseal.courseal_android.ui.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onLogin: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val loginUiState by loginViewModel.uiState.collectAsState()

    ErrorDialog(
        isVisible = loginUiState.errorState != LoginUiError.NONE,
        hideDialog = loginViewModel::hideError,
        title = when (loginUiState.errorState) {
            LoginUiError.INCORRECT -> stringResource(R.string.incorrect_usertag_or_password)
            LoginUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            LoginUiError.NONE -> ""
        }
    )

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        GoBack(onGoBack = onGoBack)

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .adaptiveContainerWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    text = stringResource(R.string.sign_in),
                    style = MaterialTheme.typography.displayMedium
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = loginUiState.serverName,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = loginUiState.serverDescription,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = loginUiState.serverUrl,
                    style = MaterialTheme.typography.bodyMedium
                )

                CoursealTextField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    enabled = loginUiState.usertagEditable,
                    value = loginUiState.usertag,
                    onValueChange = { loginViewModel.updateUsertag(it) },
                    label = stringResource(R.string.usertag),
                    leadingIcon = { Text("@") }
                )

                CoursealPasswordField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    value = loginUiState.password,
                    onValueChange = { loginViewModel.updatePassword(it) },
                    label = stringResource(R.string.password),
                )

                CoursealPrimaryLoadingButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.login),
                    loading = loginUiState.makingRequest,
                    onClick = {
                        coroutineScope.launch {
                            loginViewModel.login(onLogin, onUnrecoverable)
                        }
                    }
                )
            }
        }
    }
}