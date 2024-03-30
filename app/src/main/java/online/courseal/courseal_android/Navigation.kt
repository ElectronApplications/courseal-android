package online.courseal.courseal_android

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import online.courseal.courseal_android.ui.screens.RegisterScreen
import online.courseal.courseal_android.ui.screens.WelcomeScreen

enum class Routes(val path: String) {
    WELCOME("welcome"),
    REGISTER("register"),
    LOGIN("login")
}

@Composable
fun MainApp(setStatusBarColor: SetStatusBarColor) {
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
        composable(
            route = "${Routes.WELCOME.path}?canGoBack={canGoBack}",
            arguments = listOf(
                navArgument("canGoBack") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val canGoBack = backStackEntry.arguments?.getBoolean("canGoBack")

            WelcomeScreen(
                setStatusBarColor = setStatusBarColor,
                onGoBack = if (canGoBack == true) {{
                    navController.popBackStack()
                }} else null,
                onStart = {
                    navController.navigate(Routes.REGISTER.path)
                }
            )
        }

        composable(
            route = Routes.REGISTER.path
        ) {
            RegisterScreen(
                setStatusBarColor = setStatusBarColor
            )
        }
    }

}