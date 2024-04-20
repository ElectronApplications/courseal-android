package online.courseal.courseal_android.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.viewmodels.TopLevelUiError
import online.courseal.courseal_android.ui.viewmodels.TopLevelViewModel

enum class NavItems(val icon: ImageVector, val titleId: Int) {
    COURSE(Icons.Filled.Home, R.string.course),
    PROFILE(Icons.Filled.Face, R.string.profile),
    EDITOR(Icons.Filled.Build, R.string.editor)
}

enum class NavBarOptions(val value: Int) {
    NONE(0),
    HIDE(1),
    SHOW(2)
}

typealias OnUnrecoverable = (unrecoverableType: UnrecoverableErrorType) -> Unit

@Composable
fun TopLevelNavigation(topLevelViewModel: TopLevelViewModel = hiltViewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val topLevelUiState by topLevelViewModel.uiState.collectAsState()
    val navController = rememberNavController()

    /* Unrecoverable errors */
    val onUnrecoverable: OnUnrecoverable = {
        coroutineScope.launch {
            navController.navigate(topLevelViewModel.processUnrecoverable(it).path) {
                popUpTo(0)
            }
        }
    }
    
    ErrorDialog(
        isVisible = topLevelUiState.errorState != TopLevelUiError.NONE,
        hideDialog = topLevelViewModel::hideError,
        title = when (topLevelUiState.errorState) {
            TopLevelUiError.REFRESH_INVALID -> stringResource(R.string.session_expired)
            TopLevelUiError.SERVER_NOT_RESPONDING -> stringResource(R.string.server_not_responding)
            TopLevelUiError.OTHER_UNRECOVERABLE -> stringResource(R.string.unexpected_error)
            TopLevelUiError.NONE -> ""
        }
    )

    /* Loading screen */
    AnimatedVisibility(
        visible = topLevelUiState.isLoading || topLevelUiState.errorState != TopLevelUiError.NONE
    ) {
        TopLevelLoadingScreen()
    }

    /* Navigation */
    AnimatedVisibility(
        visible = !topLevelUiState.isLoading && topLevelUiState.errorState == TopLevelUiError.NONE
    ) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = topLevelUiState.navBarShown,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    HorizontalDivider()
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
                        NavItems.entries.forEach { entry ->
                            NavigationBarItem(
                                selected = topLevelUiState.selectedNavEntry == entry,
                                onClick = { topLevelViewModel.selectNavBarEntry(entry, navController) },
                                icon = { Icon(imageVector = entry.icon, contentDescription = stringResource(entry.titleId)) },
                                label = { Text(stringResource(entry.titleId)) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            AppNavigation(
                modifier = Modifier
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .fillMaxSize(),
                navController = navController,
                topLevelViewModel = topLevelViewModel,
                onUnrecoverable = onUnrecoverable
            )
        }
    }
}

fun NavGraphBuilder.coursealRoute(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    transitionFadeDefault: Boolean = false,
    navBarDefault: NavBarOptions = NavBarOptions.NONE,
    setNavBarShown: (Boolean) -> Unit,
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) {
    val newRoute = if (route.contains('?'))
        "${route}&transitionFade={transitionFade}&navBar={navBar}"
    else
        "${route}?transitionFade={transitionFade}&navBar={navBar}"

    val newArguments = arguments.toMutableList().apply { addAll(listOf(
        navArgument("transitionFade") {
            type = NavType.BoolType
            defaultValue = transitionFadeDefault
        },
        navArgument("navBar") {
            type = NavType.IntType
            defaultValue = navBarDefault.value
        }
    )) }

    val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? = {
        val transitionFade = targetState.arguments?.getBoolean("transitionFade")
        if (transitionFade == true)
            fadeIn()
        else
            null
    }

    val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? = {
        val transitionFade = targetState.arguments?.getBoolean("transitionFade")
        if (transitionFade == true)
            fadeOut()
        else
            null
    }

    composable(
        route = newRoute,
        arguments = newArguments,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) { backStackEntry ->
        val navBar = backStackEntry.arguments?.getInt("navBar")
        if (navBar != null && navBar != NavBarOptions.NONE.value) {
            setNavBarShown(navBar == NavBarOptions.SHOW.value)
        }

        content(this, backStackEntry)
    }
}

@Composable
fun TopLevelLoadingScreen() {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .width(150.dp)
                .align(alignment = Alignment.CenterHorizontally),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = R.drawable.courseal_not_rounded),
            contentDescription = stringResource(R.string.logo)
        )

        Text(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}