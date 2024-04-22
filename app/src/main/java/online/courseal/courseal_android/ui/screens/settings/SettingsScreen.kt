package online.courseal.courseal_android.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.GoBack
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onChangeProfilePicture: () -> Unit,
    onChangeUsername: () -> Unit,
    onChangePassword: () -> Unit,
    onViewAccounts: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        GoBack(onGoBack = onGoBack)

        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            CoursealOutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterHorizontally)
            ) {
                CoursealOutlinedCardItem(
                    modifier = Modifier.clickable(onClick = onChangeProfilePicture),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.change_profile_picture)
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(20.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.change_profile_picture),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }

                CoursealOutlinedCardItem(
                    modifier = Modifier.clickable(onClick = onChangeUsername),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.change_username)
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(20.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.change_username),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }

                CoursealOutlinedCardItem(
                    modifier = Modifier.clickable(onClick = onChangePassword),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.change_password)
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(20.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.change_password),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }

                CoursealOutlinedCardItem(
                    modifier = Modifier.clickable(onClick = onViewAccounts),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = stringResource(R.string.view_accounts)
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(20.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.view_accounts),
                        contentScale = ContentScale.FillWidth,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}