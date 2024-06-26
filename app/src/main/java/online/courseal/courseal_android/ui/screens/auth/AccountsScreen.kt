package online.courseal.courseal_android.ui.screens.auth

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealSecondaryButton
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.auth.AccountsViewModel

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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .adaptiveContainerWidth()
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.choose_account),
                style = MaterialTheme.typography.displayMedium
            )

            CoursealOutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
            ) {
                accountsUiState.accounts.forEach { account ->
                    CoursealOutlinedCardItem(
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                accountsViewModel.chooseAccount(
                                    userId = account.userId,
                                    onLoggedIn = onLoggedIn,
                                    onNotLoggedIn = onNotLoggedIn
                                )
                            }
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f, fill = false)
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
                                text = if (account.loggedIn) stringResource(R.string.logged_in) else stringResource(
                                    R.string.not_logged_in
                                ),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(
                            modifier = Modifier
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
                }
            }

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