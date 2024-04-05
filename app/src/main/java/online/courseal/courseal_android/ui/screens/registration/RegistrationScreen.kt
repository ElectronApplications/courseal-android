package online.courseal.courseal_android.ui.screens.registration

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.components.CoursealPasswordField
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.GoBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette
import online.courseal.courseal_android.ui.viewmodels.RegistrationViewModel
import online.courseal.courseal_android.ui.viewmodels.WelcomeViewModel

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onStartLogin: (serverId: Long) -> Unit,
    onRegister: () -> Unit,
    registrationViewModel: RegistrationViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val registrationUiState by registrationViewModel.uiState.collectAsState()

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
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.displayMedium
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = registrationUiState.serverName,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = registrationUiState.serverDescription,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    text = registrationUiState.serverUrl,
                    style = MaterialTheme.typography.bodyMedium
                )

                var usertag by rememberSaveable { mutableStateOf("") }
                CoursealTextField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    value = usertag,
                    onValueChange = { usertag = it },
                    label = stringResource(R.string.usertag),
                    leadingIcon = { Text("@") }
                )

                var username by rememberSaveable { mutableStateOf("") }
                CoursealTextField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    value = username,
                    onValueChange = { username = it },
                    label = stringResource(R.string.username)
                )

                var password by rememberSaveable { mutableStateOf("") }
                CoursealPasswordField(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                        .fillMaxWidth(),
                    value = password,
                    onValueChange = { password = it },
                    label = stringResource(R.string.password),
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 15.dp)
                        .clickable {
                            onStartLogin(registrationViewModel.getServerId())
                        },
                    color = LocalCoursealPalette.current.link,
                    text = stringResource(R.string.already_have_account)
                )

                CoursealPrimaryButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 15.dp)
                        .fillMaxWidth(),
                    text = stringResource(R.string.register),
                    onClick = {
                        /* TODO */
                    }
                )

                Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
            }
        }
    }
}