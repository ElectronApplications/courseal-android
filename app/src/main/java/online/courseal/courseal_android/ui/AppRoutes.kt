package online.courseal.courseal_android.ui

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import online.courseal.courseal_android.ui.screens.auth.AccountsScreen
import online.courseal.courseal_android.ui.screens.auth.LoginScreen
import online.courseal.courseal_android.ui.screens.auth.RegistrationScreen
import online.courseal.courseal_android.ui.screens.course.CourseScreen
import online.courseal.courseal_android.ui.screens.editor.EditorScreen
import online.courseal.courseal_android.ui.screens.profile.ProfileCoursesScreen
import online.courseal.courseal_android.ui.screens.profile.ProfileScreen
import online.courseal.courseal_android.ui.screens.welcome.WelcomeScreen
import online.courseal.courseal_android.ui.viewmodels.ProfileScreenViewModel
import online.courseal.courseal_android.ui.viewmodels.TopLevelViewModel

enum class Routes(val path: String) {
    WELCOME("welcome"),
    REGISTER("register"),
    LOGIN("login"),
    ACCOUNTS("accounts"),
    COURSE("course"),
    PROFILE("profile"),
    EDITOR("editor"),
    PROFILE_COURSES("profile-courses")
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    topLevelViewModel: TopLevelViewModel,
    onUnrecoverable: OnUnrecoverable
) {
    val topLevelUiState by topLevelViewModel.uiState.collectAsState()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = topLevelUiState.startDestination.path,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
    ) {
        /* Welcome Screen */
        coursealRoute(
            route = "${Routes.WELCOME.path}?canGoBack={canGoBack}",
            arguments = listOf(
                navArgument("canGoBack") { type = NavType.BoolType; defaultValue = false },
            ),
            navBarDefault = NavBarOptions.HIDE,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) { backStackEntry ->
            val canGoBack = backStackEntry.arguments?.getBoolean("canGoBack")

            WelcomeScreen(
                onGoBack = if (canGoBack == true) {{ navController.popBackStack() }} else null,
                onStart = { serverRegistrationEnabled: Boolean, serverId: Long ->
                    if (serverRegistrationEnabled)
                        navController.navigate("${Routes.REGISTER.path}?serverId=$serverId")
                    else
                        navController.navigate("${Routes.LOGIN.path}?serverId=$serverId")
                }
            )
        }

        /* Registration Screen */
        coursealRoute(
            route = "${Routes.REGISTER.path}?serverId={serverId}",
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType },
            ),
            navBarDefault = NavBarOptions.HIDE,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            RegistrationScreen(
                onGoBack = { navController.popBackStack() },
                onStartLogin = { serverId: Long ->
                    navController.navigate("${Routes.LOGIN.path}?serverId=${serverId}")
                },
                onRegister = {
                    navController.navigate(Routes.COURSE.path) {
                        popUpTo(0)
                    }
                },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Login Screen */
        coursealRoute(
            route = "${Routes.LOGIN.path}?serverId={serverId}",
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType },
            ),
            navBarDefault = NavBarOptions.HIDE,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            LoginScreen(
                onGoBack = { navController.popBackStack() },
                onLogin = {
                    navController.navigate(Routes.COURSE.path) {
                        popUpTo(0)
                    }
                },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Accounts Screen */
        coursealRoute(
            route = Routes.ACCOUNTS.path,
            navBarDefault = NavBarOptions.HIDE,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            AccountsScreen(
                onLoggedIn = {
                    navController.navigate(Routes.COURSE.path) {
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

        /* Course Screen */
        coursealRoute(
            route = Routes.COURSE.path,
            transitionFadeDefault = true,
            navBarDefault = NavBarOptions.SHOW,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            CourseScreen()
        }

        /* Profile Screen */
        coursealRoute(
            route = "${Routes.PROFILE.path}?canGoBack={canGoBack}&usertag={usertag}&isCurrent={isCurrent}",
            arguments = listOf(
                navArgument("canGoBack") { type = NavType.BoolType; defaultValue = false },
                navArgument("usertag") { type = NavType.StringType; nullable = true ; defaultValue = null },
                navArgument("isCurrent") { type = NavType.BoolType; defaultValue = false }
            ),
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) { backStackEntry ->
            val canGoBack = backStackEntry.arguments?.getBoolean("canGoBack")

            ProfileScreen(
                onViewAccounts = if(canGoBack != true) {{
                    navController.navigate("${Routes.ACCOUNTS.path}?transitionFade=true") {
                        popUpTo(0)
                    }
                }} else null,
                onViewCourses = {
                    navController.navigate(Routes.PROFILE_COURSES.path)
                },
                onGoBack = if (canGoBack == true) {{ navController.popBackStack() }} else null,
                onSearchUsers = { /*TODO*/ },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Creator Screen */
        coursealRoute(
            route = Routes.EDITOR.path,
            transitionFadeDefault = true,
            navBarDefault = NavBarOptions.SHOW,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            EditorScreen()
        }

        /* Profile's courses Screen */
        coursealRoute(
            route = Routes.PROFILE_COURSES.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.PROFILE.path)
            }
            val parentViewModel = hiltViewModel<ProfileScreenViewModel>(parentEntry)

            ProfileCoursesScreen(
                onGoBack = { navController.popBackStack() },
                onOpenCourse = { /*TODO*/ },
                onUnrecoverable = onUnrecoverable,
                profileScreenViewModel = parentViewModel
            )
        }
    }
}