package online.courseal.courseal_android.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.screens.auth.AccountsScreen
import online.courseal.courseal_android.ui.screens.auth.LoginScreen
import online.courseal.courseal_android.ui.screens.main.MainScreen
import online.courseal.courseal_android.ui.screens.auth.RegistrationScreen
import online.courseal.courseal_android.ui.screens.welcome.WelcomeScreen
import online.courseal.courseal_android.ui.viewmodels.TopLevelUiError
import online.courseal.courseal_android.ui.viewmodels.TopLevelViewModel

enum class Routes(val path: String) {
    WELCOME("welcome"),
    REGISTER("register"),
    LOGIN("login"),
    ACCOUNTS("accounts"),
    MAIN("main")
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

    /* Defining transitions */
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

    val transitionFadeArgument = navArgument("transitionFade") {
        type = NavType.BoolType
        defaultValue = false
    }

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
        NavHost(
            navController = navController,
            startDestination = topLevelUiState.startDestination.path,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            /* Welcome Screen */
            composable(
                route = "${Routes.WELCOME.path}?canGoBack={canGoBack}&transitionFade={transitionFade}",
                arguments = listOf(
                    navArgument("canGoBack") { type = NavType.BoolType; defaultValue = false },
                    transitionFadeArgument
                ),
                enterTransition = enterTransition,
                exitTransition = exitTransition
            ) { backStackEntry ->
                val canGoBack = backStackEntry.arguments?.getBoolean("canGoBack")

                WelcomeScreen(
                    onGoBack = if (canGoBack == true) {{
                        navController.popBackStack()
                    }} else null,
                    onStart = { serverRegistrationEnabled: Boolean, serverId: Long ->
                        if (serverRegistrationEnabled)
                            navController.navigate("${Routes.REGISTER.path}?serverId=$serverId")
                        else
                            navController.navigate("${Routes.LOGIN.path}?serverId=$serverId")
                    }
                )
            }

            /* Registration Screen */
            composable(
                route = "${Routes.REGISTER.path}?serverId={serverId}&transitionFade={transitionFade}",
                arguments = listOf(
                    navArgument("serverId") { type = NavType.LongType },
                    transitionFadeArgument
                ),
                enterTransition = enterTransition,
                exitTransition = exitTransition
            ) {
                RegistrationScreen(
                    onGoBack = {
                        navController.popBackStack()
                    },
                    onStartLogin = { serverId: Long ->
                        navController.navigate("${Routes.LOGIN.path}?serverId=${serverId}")
                    },
                    onRegister = {
                        navController.navigate("${Routes.MAIN.path}?transitionFade=true") {
                            popUpTo(0)
                        }
                    },
                    onUnrecoverable = onUnrecoverable
                )
            }

            /* Login Screen */
            composable(
                route = "${Routes.LOGIN.path}?serverId={serverId}&transitionFade={transitionFade}",
                arguments = listOf(
                    navArgument("serverId") { type = NavType.LongType },
                    transitionFadeArgument
                ),
                enterTransition = enterTransition,
                exitTransition = exitTransition
            ) {
                LoginScreen(
                    onGoBack = {
                        navController.popBackStack()
                    },
                    onLogin = {
                        navController.navigate("${Routes.MAIN.path}?transitionFade=true") {
                            popUpTo(0)
                        }
                    },
                    onUnrecoverable = onUnrecoverable
                )
            }

            /* Accounts Screen */
            composable(
                route = "${Routes.ACCOUNTS.path}?transitionFade={transitionFade}",
                arguments = listOf(transitionFadeArgument),
                enterTransition = enterTransition,
                exitTransition = exitTransition
            ) {
                AccountsScreen(
                    onLoggedIn = {
                        navController.navigate("${Routes.MAIN.path}?transitionFade=true") {
                            popUpTo(0)
                        }
                    },
                    onNotLoggedIn = { serverId: Long ->
                        navController.navigate("${Routes.LOGIN.path}?serverId=${serverId}")
                    },
                    onAllAccountsDeleted = {
                        navController.navigate("${Routes.WELCOME.path}?transitionFade=true") {
                            popUpTo(0)
                        }
                    },
                    onAddNewAccount = {
                        navController.navigate("${Routes.WELCOME.path}?canGoBack=true")
                    },
                    onUnrecoverable = onUnrecoverable
                )
            }

            /* Main Screen */
            composable(
                route = "${Routes.MAIN.path}?transitionFade={transitionFade}",
                arguments = listOf(transitionFadeArgument),
                enterTransition = enterTransition,
                exitTransition = exitTransition
            ) {
                MainScreen(
                    onViewAccounts = {
                        navController.navigate("${Routes.ACCOUNTS.path}?transitionFade=true") {
                            popUpTo(0)
                        }
                    },
                    onUnrecoverable = onUnrecoverable
                )
            }
        }
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