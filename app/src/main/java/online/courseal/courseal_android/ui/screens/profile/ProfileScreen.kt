package online.courseal.courseal_android.ui.screens.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealSecondaryButton
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.profile.ProfileViewModel
import online.courseal.courseal_android.ui.viewmodels.profile.ProfileUiError

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onGoBack: (() -> Unit)? = null,
    onViewSettings: (() -> Unit)? = null,
    onViewCourses: () -> Unit,
    onSearchUsers: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val profileUiState by profileViewModel.uiState.collectAsState()

    if (profileUiState.errorUnrecoverableState != null) {
        onUnrecoverable(profileUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = profileUiState.errorState != ProfileUiError.NONE,
        hideDialog = profileViewModel::hideError,
        title = when (profileUiState.errorState) {
            ProfileUiError.USER_NOT_FOUND -> stringResource(R.string.user_not_found)
            ProfileUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            ProfileUiError.NONE -> ""
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (onViewSettings != null) {
            CoursealTopBar {
                Row(
                    modifier = Modifier
                        .clickable {
                            onViewSettings()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.settings)
                    )
                }
            }
        } else if (onGoBack != null) {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
            TopBack(onClick = onGoBack)
        }

        if (profileUiState.loading) {
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
                val userPublicInfo = profileUiState.userPublicInfo
                val userPrivateInfo = profileUiState.userPrivateInfo
                val userActivity = profileUiState.userActivity

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
                            contentDescription = userPublicInfo.usertag,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
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
                        CoursealOutlinedCard(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 20.dp)
                                .fillMaxWidth(0.85f)
                        ) {
                            CoursealOutlinedCardItem {
                                ProfileActivity(
                                    modifier = Modifier
                                        .height(120.dp),
                                    userActivity = userActivity
                                )
                            }
                        }
                    }

                    if (userPublicInfo != null) {
                        CoursealOutlinedCard(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f)
                        ) {
                            CoursealOutlinedCardItem(
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f, fill = false),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier.width(32.dp),
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "XP",
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(Color(0xFFF7C200))
                                    )

                                    Column(
                                        modifier = Modifier.padding(start = 6.dp),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${userPublicInfo.xp}",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = stringResource(R.string.xp_earned),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }

                                Row(
                                    modifier = Modifier.clickable(onClick = onViewCourses),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${userPublicInfo.courses.size} ${stringResource(R.string.courses)}")
                                    Image(
                                        modifier = Modifier
                                            .padding(start = 4.dp)
                                            .width(20.dp),
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = stringResource(R.string.view_courses),
                                        contentScale = ContentScale.FillWidth,
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                                    )
                                }
                            }
                        }
                    }

                    if (profileUiState.isCurrent && onGoBack == null) {
                        CoursealSecondaryButton(
                            modifier = Modifier
                                .padding(top = 20.dp)
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            text = stringResource(R.string.find_users),
                            onClick = onSearchUsers
                        )
                    }
                }
            }
        }
    }
}