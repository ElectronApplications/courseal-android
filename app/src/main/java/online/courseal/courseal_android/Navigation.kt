package online.courseal.courseal_android

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import online.courseal.courseal_android.ui.screens.login.LoginScreen
import online.courseal.courseal_android.ui.screens.registration.RegistrationScreen
import online.courseal.courseal_android.ui.screens.welcome.WelcomeScreen

enum class Routes(val path: String) {
    WELCOME("welcome"),
    REGISTER("register"),
    LOGIN("login")
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME.path,
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it })
        }
    ) {
        /* Welcome Screen */
        composable(
            route = "${Routes.WELCOME.path}?canGoBack={canGoBack}",
            arguments = listOf(
                navArgument("canGoBack") { type = NavType.BoolType; defaultValue = false }
            )
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
            route = "${Routes.REGISTER.path}?serverId={serverId}",
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType }
            )
        ) {
            RegistrationScreen(
                onGoBack = {
                    navController.popBackStack()
                },
                onStartLogin = { serverId: Long ->
                   navController.navigate("${Routes.LOGIN.path}?serverId=${serverId}")
                },
                onRegister = {

                }
            )
        }

        /* Login Screen */
        composable(
            route = "${Routes.LOGIN.path}?serverId={serverId}",
            arguments = listOf(
                navArgument("serverId") { type = NavType.LongType }
            )
        ) {
            LoginScreen(
                onGoBack = {
                    navController.popBackStack()
                },
                onLogin = {
                    navController.navigate(Routes.WELCOME.path) {
                        popUpTo(0)
                    }
                }
            )
        }
    }

}