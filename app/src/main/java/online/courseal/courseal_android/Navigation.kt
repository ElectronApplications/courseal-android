package online.courseal.courseal_android

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import online.courseal.courseal_android.data.api.ServerInfo
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
        startDestination = Routes.WELCOME.path
    ) {
        composable(
            route = Routes.WELCOME.path
        ) {
            WelcomeScreen(setStatusBarColor = setStatusBarColor, onStart = { _: String, _: ServerInfo -> /* TODO */ })
        }
    }

}