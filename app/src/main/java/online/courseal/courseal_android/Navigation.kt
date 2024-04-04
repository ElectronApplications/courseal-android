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
import online.courseal.courseal_android.data.editorjs.EditorJSCode
import online.courseal.courseal_android.data.editorjs.EditorJSCodeData
import online.courseal.courseal_android.data.editorjs.EditorJSContent
import online.courseal.courseal_android.data.editorjs.EditorJSDelimiter
import online.courseal.courseal_android.data.editorjs.EditorJSHeader
import online.courseal.courseal_android.data.editorjs.EditorJSHeaderData
import online.courseal.courseal_android.data.editorjs.EditorJSHeaderLevel
import online.courseal.courseal_android.data.editorjs.EditorJSImage
import online.courseal.courseal_android.data.editorjs.EditorJSImageData
import online.courseal.courseal_android.data.editorjs.EditorJSLatex
import online.courseal.courseal_android.data.editorjs.EditorJSLatexData
import online.courseal.courseal_android.data.editorjs.EditorJSList
import online.courseal.courseal_android.data.editorjs.EditorJSListData
import online.courseal.courseal_android.data.editorjs.EditorJSListStyle
import online.courseal.courseal_android.data.editorjs.EditorJSParagraph
import online.courseal.courseal_android.data.editorjs.EditorJSParagraphData
import online.courseal.courseal_android.data.editorjs.EditorJSQuote
import online.courseal.courseal_android.data.editorjs.EditorJSQuoteAlignment
import online.courseal.courseal_android.data.editorjs.EditorJSQuoteData
import online.courseal.courseal_android.data.editorjs.EditorJSWarning
import online.courseal.courseal_android.data.editorjs.EditorJSWarningData
import online.courseal.courseal_android.data.editorjs.EditorJsImageFile
import online.courseal.courseal_android.ui.components.editorjs.content.EditorJSContentComponent
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
                onStart = { serverRegistrationEnabled: Boolean ->
                    if (serverRegistrationEnabled)
                        navController.navigate(Routes.REGISTER.path)
                    else
                        navController.navigate(Routes.LOGIN.path)
                }
            )
        }

        /* Registration Screen */
        composable(
            route = Routes.REGISTER.path
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.WELCOME.path)
            }
            RegistrationScreen(
                onGoBack = {
                    navController.popBackStack()
                },
                onStartLogin = {
                   navController.navigate(Routes.LOGIN.path)
                },
                onRegister = {

                },
                authViewModel = hiltViewModel(parentEntry)
            )
        }

        /* Login Screen */
        composable(
            route = Routes.LOGIN.path
        ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Routes.WELCOME.path)
            }
            LoginScreen(
                onGoBack = {
                    navController.popBackStack()
                },
                onLogin = {

                },
                authViewModel = hiltViewModel(parentEntry)
            )
        }
    }

}