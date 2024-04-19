package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.api.user.UserApiResponse
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.GoBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.ProfileScreenViewModel
import online.courseal.courseal_android.ui.viewmodels.ProfileUiError

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    onViewAccounts: (() -> Unit)? = null,
    onUnrecoverable: OnUnrecoverable,
    profileScreenViewModel: ProfileScreenViewModel = hiltViewModel()
) {
    val profileScreenUiState by profileScreenViewModel.uiState.collectAsState()

    if (profileScreenUiState.errorUnrecoverableState != null) {
        onUnrecoverable(profileScreenUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = profileScreenUiState.errorState != ProfileUiError.NONE,
        hideDialog = profileScreenViewModel::hideError,
        title = when (profileScreenUiState.errorState) {
            ProfileUiError.USER_NOT_FOUND -> stringResource(R.string.user_not_found)
            ProfileUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            ProfileUiError.NONE -> ""
        }
    )

    if (profileScreenUiState.loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            if (onViewAccounts != null) {
                CoursealTopBar {
                    Row(
                        modifier = Modifier
                            .clickable {
                                onViewAccounts()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.view_accounts),
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                            contentDescription = stringResource(R.string.view_accounts)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            if (onGoBack != null) {
                GoBack(onGoBack = onGoBack)
            }

//            val userPublicInfo = profileScreenUiState.userPublicInfo
            val userPrivateInfo = profileScreenUiState.userPrivateInfo
            val userActivity = profileScreenUiState.userActivity

            val userPublicInfo = UserApiResponse(
                usertag = "electronapps",
                username = "ElectronApps",
                dateCreated = "1234",
                canCreateCourses = true,
                xp = 100,
                profileImageUrl = "https://avatars.githubusercontent.com/u/36295932?s=400&u=a5a1e71312d9195a1184f689c2f42c4e04c9a748&v=4",
                courses = emptyList(),
                coursesMaintainer = emptyList()
            )

            Column(
                modifier = Modifier
                    .adaptiveContainerWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                if (userPublicInfo != null) {
                    AsyncImage(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.25f)
                            .clip(CircleShape),
                        model = userPublicInfo.profileImageUrl,
                        contentDescription = userPublicInfo.usertag
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = userPublicInfo.username,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "@${userPublicInfo.usertag}",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Normal)
                    )
                }
            }
        }
    }
}