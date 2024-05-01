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
import online.courseal.courseal_android.ui.screens.courseinfo.CourseInfoScreen
import online.courseal.courseal_android.ui.screens.editor.EditorScreen
import online.courseal.courseal_android.ui.screens.profile.ProfileCoursesScreen
import online.courseal.courseal_android.ui.screens.profile.ProfileScreen
import online.courseal.courseal_android.ui.screens.profile.SearchUsersScreen
import online.courseal.courseal_android.ui.screens.settings.SettingsPasswordScreen
import online.courseal.courseal_android.ui.screens.settings.SettingsPictureScreen
import online.courseal.courseal_android.ui.screens.settings.SettingsScreen
import online.courseal.courseal_android.ui.screens.settings.SettingsUsernameScreen
import online.courseal.courseal_android.ui.screens.welcome.WelcomeScreen
import online.courseal.courseal_android.ui.viewmodels.profile.ProfileViewModel
import online.courseal.courseal_android.ui.viewmodels.TopLevelViewModel

enum class Routes(val path: String) {
    WELCOME("welcome"),
    REGISTER("register"),
    LOGIN("login"),
    ACCOUNTS("accounts"),

    COURSE("course"),
    PROFILE("profile"),
    EDITOR("editor"),

    PROFILE_SETTINGS("profile/settings"),
    PROFILE_SETTINGS_PICTURE("profile/settings/picture"),
    PROFILE_SETTINGS_USERNAME("profile/settings/username"),
    PROFILE_SETTINGS_PASSWORD("profile/settings/password"),
    PROFILE_COURSES("profile/courses"),

    SEARCH_USERS("search-users"),
    SEARCH_COURSES("search-courses"),

    COURSE_INFO("course-info"),
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
                onViewSettings = if(canGoBack != true) {{
                    navController.navigate(Routes.PROFILE_SETTINGS.path)
                }} else null,
                onViewCourses = {
                    navController.navigate(Routes.PROFILE_COURSES.path)
                },
                onGoBack = if (canGoBack == true) {{ navController.popBackStack() }} else null,
                onSearchUsers = {
                    navController.navigate(Routes.SEARCH_USERS.path)
                },
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
            EditorScreen(
                onCreateCourse = { /* TODO */ },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Profile's settings Screen */
        coursealRoute(
            route = Routes.PROFILE_SETTINGS.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            SettingsScreen(
                onGoBack = { navController.popBackStack() },
                onChangeProfilePicture = {
                    navController.navigate(Routes.PROFILE_SETTINGS_PICTURE.path)
                },
                onChangeUsername = {
                    navController.navigate(Routes.PROFILE_SETTINGS_USERNAME.path)
                },
                onChangePassword = {
                    navController.navigate(Routes.PROFILE_SETTINGS_PASSWORD.path)
                },
                onViewAccounts = {
                    navController.clearBackStack(Routes.COURSE.path)
                    navController.clearBackStack(Routes.PROFILE.path)
                    navController.clearBackStack(Routes.EDITOR.path)
                    navController.navigate("${Routes.ACCOUNTS.path}?transitionFade=true") {
                        popUpTo(0)
                    }
                },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Profile's settings profile picture Screen */
        coursealRoute(
            route = Routes.PROFILE_SETTINGS_PICTURE.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            SettingsPictureScreen(
                onGoBack = { navController.popBackStack() },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Profile's settings profile username Screen */
        coursealRoute(
            route = Routes.PROFILE_SETTINGS_USERNAME.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.PROFILE.path)
            }
            val parentViewModel = hiltViewModel<ProfileViewModel>(parentEntry)

            SettingsUsernameScreen(
                onGoBack = { navController.popBackStack() },
                onUnrecoverable = onUnrecoverable,
                profileViewModel = parentViewModel
            )
        }

        /* Profile's settings profile password Screen */
        coursealRoute(
            route = Routes.PROFILE_SETTINGS_PASSWORD.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            SettingsPasswordScreen(
                onGoBack = { navController.popBackStack() },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Profile's courses Screen */
        coursealRoute(
            route = Routes.PROFILE_COURSES.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.PROFILE.path)
            }
            val parentViewModel = hiltViewModel<ProfileViewModel>(parentEntry)

            ProfileCoursesScreen(
                onGoBack = { navController.popBackStack() },
                onOpenCourse = { courseId ->
                    navController.navigate("${Routes.COURSE_INFO.path}/${courseId}")
                },
                onUnrecoverable = onUnrecoverable,
                profileViewModel = parentViewModel
            )
        }

        /* Search users Screen */
        coursealRoute(
            route = Routes.SEARCH_USERS.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            SearchUsersScreen(
                onGoBack = { navController.popBackStack() },
                onOpenProfile = { usertag ->
                    navController.navigate("${Routes.PROFILE.path}?usertag=${usertag}&canGoBack=true")
                },
                onUnrecoverable = onUnrecoverable
            )
        }

        /* Search courses Screen */
        coursealRoute(
            route = Routes.SEARCH_COURSES.path,
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            // TODO
        }

        /* Course info Screen  */
        coursealRoute(
            route = "${Routes.COURSE_INFO.path}/{courseId}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType }
            ),
            setNavBarShown = topLevelViewModel::setNavBarShown
        ) {
            CourseInfoScreen(
                onGoBack = { navController.popBackStack() },
                onOpenProfile = { usertag ->
                    navController.navigate("${Routes.PROFILE.path}?usertag=$usertag&canGoBack=true")
                },
                onUnrecoverable = onUnrecoverable
            )
        }
    }
}