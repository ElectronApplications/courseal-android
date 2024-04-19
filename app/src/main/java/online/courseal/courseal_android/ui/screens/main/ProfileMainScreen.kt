package online.courseal.courseal_android.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealTopBar
import online.courseal.courseal_android.ui.screens.profile.ProfileScreen

@Composable
fun ProfileMainScreen(
    modifier: Modifier = Modifier,
    onViewAccounts: () -> Unit,
    onUnrecoverable: OnUnrecoverable
) {
    Column(
        modifier = modifier
    ) {
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

        ProfileScreen(
            onUnrecoverable = onUnrecoverable
        )
    }
}