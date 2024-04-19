package online.courseal.courseal_android.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable

enum class NavItems(val icon: ImageVector, val titleId: Int) {
    COURSE(Icons.Filled.Home, R.string.course),
    PROFILE(Icons.Filled.Face, R.string.profile),
    EDITOR(Icons.Filled.Build, R.string.editor)
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    onViewAccounts: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
) {
    var selectedEntry by rememberSaveable { mutableStateOf(NavItems.COURSE) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            HorizontalDivider()
            NavigationBar(
                containerColor = Color.Transparent
            ) {
                NavItems.entries.forEach { entry ->
                    NavigationBarItem(
                        selected = selectedEntry == entry,
                        onClick = {
                            selectedEntry = entry
                        },
                        icon = { Icon(imageVector = entry.icon, contentDescription = stringResource(entry.titleId)) },
                        label = { Text(stringResource(entry.titleId)) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            AnimatedVisibility(
                visible = selectedEntry == NavItems.COURSE,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CourseMainScreen(
                    onUnrecoverable = onUnrecoverable
                )
            }

            AnimatedVisibility(
                visible = selectedEntry == NavItems.PROFILE,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ProfileMainScreen(
                    onViewAccounts = onViewAccounts,
                    onUnrecoverable = onUnrecoverable
                )
            }

            AnimatedVisibility(
                visible = selectedEntry == NavItems.EDITOR,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EditorMainScreen(
                    onUnrecoverable = onUnrecoverable
                )
            }
        }
    }
}