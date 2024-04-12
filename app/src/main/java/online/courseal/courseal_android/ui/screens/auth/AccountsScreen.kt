package online.courseal.courseal_android.ui.screens.auth

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealSecondaryButton
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.AccountsViewModel

@Composable
fun AccountsScreen(
    modifier: Modifier = Modifier,
    onLoggedIn: () -> Unit,
    onNotLoggedIn: (serverId: Long) -> Unit,
    onAllAccountsDeleted: () -> Unit,
    onAddNewAccount: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    accountsViewModel: AccountsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val accountsUiState by accountsViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        Text(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.choose_account),
            style = MaterialTheme.typography.displayMedium
        )

        HorizontalDivider()
        accountsUiState.accounts.forEach { account ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coroutineScope.launch {
                            accountsViewModel.chooseAccount(
                                account.userId,
                                onLoggedIn,
                                onNotLoggedIn
                            )
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp, start = 20.dp)
                ) {
                    Text(
                        text = "@${account.usertag}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = account.serverUrl,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (account.loggedIn) stringResource(R.string.logged_in) else stringResource(R.string.not_logged_in),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp, end = 20.dp)
                        .scale(1.25f)
                        .clickable {
                            coroutineScope.launch {
                                accountsViewModel.removeAccount(
                                    account.userId,
                                    onAllAccountsDeleted
                                )
                            }
                        },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.delete_account),
                )
            }
            HorizontalDivider()
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .adaptiveContainerWidth()
        ) {
            CoursealSecondaryButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 15.dp)
                    .fillMaxWidth(0.75f),
                text = stringResource(R.string.add_account),
                onClick = {
                    coroutineScope.launch { accountsViewModel.addNewAccount(onAddNewAccount) }
                }
            )
        }
    }
}