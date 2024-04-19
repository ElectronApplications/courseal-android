package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.api.user.UserActivityApiResponse
import online.courseal.courseal_android.data.api.user.UserActivityDay
import online.courseal.courseal_android.data.api.user.UserApiResponse
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.GoBack
import online.courseal.courseal_android.ui.components.TextHTML
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        } else if (onGoBack != null) {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            GoBack(onGoBack = onGoBack)
        }

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
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                val userPublicInfo = profileScreenUiState.userPublicInfo
                val userPrivateInfo = profileScreenUiState.userPrivateInfo
                val userActivity = profileScreenUiState.userActivity

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
                            placeholder = rememberVectorPainter(Icons.Filled.AccountCircle),
                            error = rememberVectorPainter(Icons.Filled.AccountCircle),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = userPublicInfo.usertag
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            text = userPublicInfo.username,
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            text = "@${userPublicInfo.usertag}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            text = "${stringResource(R.string.joined)} ${userPublicInfo.dateCreated.date}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }

                    if (userActivity != null) {
                        ProfileActivity(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 20.dp)
                                .fillMaxWidth(0.85f)
                                .height(120.dp),
                            userActivity = userActivity
                        )
                    }

                    if (userPublicInfo != null) {
                        TextHTML(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            text = "${stringResource(R.string.xp_earned)} <strong>${userPublicInfo.xp}</strong>",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}