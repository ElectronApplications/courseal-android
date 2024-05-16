package online.courseal.courseal_android.ui.screens.profile

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.TopBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.profile.SearchUsersViewModel

@Composable
fun SearchUsersScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onOpenProfile: (usertag: String) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    searchUsersViewModel: SearchUsersViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val searchUsersUiState by searchUsersViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TopBack(onClick = onGoBack)

        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
                .adaptiveContainerWidth()
        ) {
            CoursealTextField(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.85f),
                value = searchUsersUiState.searchTerm,
                onValueChange = searchUsersViewModel::updateSearchTerm,
                label = stringResource(R.string.username_or_usertag)
            )

            CoursealPrimaryButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.85f),
                text = stringResource(R.string.search),
                onClick = {
                    coroutineScope.launch {
                        searchUsersViewModel.search(onUnrecoverable)
                    }
                }
            )

            CoursealOutlinedCard(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
                    .fillMaxWidth(0.85f)
            ) {
                LazyColumn {
                    items(searchUsersUiState.users) {user ->
                        CoursealOutlinedCardItem(
                            modifier = Modifier.clickable { onOpenProfile(user.usertag) },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                Text(
                                    text = user.username,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "@${user.usertag}"
                                )
                            }
                            Text(
                                text = "${user.xp} XP"
                            )
                        }
                    }
                }
            }
        }
    }
}