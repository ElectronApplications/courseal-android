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
                onRegister = {},
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
                    navController.navigate("editorjs")
                },
                authViewModel = hiltViewModel(parentEntry)
            )
        }

        composable(
            route = "editorjs"
        ) {
            val sampleContent = EditorJSContent(
                time = 1711507166253,
                version = "2.29.1",
                blocks = listOf(
                    EditorJSHeader(
                        id = "oi2FNWysnU",
                        data = EditorJSHeaderData(
                            text = "Sample Header h1",
                            level = EditorJSHeaderLevel.H1
                        )
                    ),
                    EditorJSHeader(
                        id = "c03rFjLjll",
                        data = EditorJSHeaderData(
                            text = "Sample Header h6",
                            level = EditorJSHeaderLevel.H6
                        )
                    ),
                    EditorJSParagraph(
                        id = "Y0FC-KXE9f",
                        data = EditorJSParagraphData(
                            text = "Sample text<br>"
                        )
                    ),
                    EditorJSParagraph(
                        id = "nNEesxxc6H",
                        data = EditorJSParagraphData(
                            text = "<i>Sample italic text</i><br>"
                        )
                    ),
                    EditorJSParagraph(
                        id = "nNEesxxc6H",
                        data = EditorJSParagraphData(
                            text = "<b>Sample bold text</b><br>"
                        )
                    ),
                    EditorJSParagraph(
                        id = "nNEesxxc6H",
                        data = EditorJSParagraphData(
                            text = "<a href=\"example.com\">Sample link</a><br>"
                        )
                    ),
                    EditorJSList(
                        id = "O6yNYMO2HR",
                        data = EditorJSListData(
                            style = EditorJSListStyle.ORDERED,
                            items = listOf(
                                "ordered",
                                "list"
                            )
                        )
                    ),
                    EditorJSList(
                        id = "PodGxsipV_",
                        data = EditorJSListData(
                            style = EditorJSListStyle.UNORDERED,
                            items = listOf(
                                "unordered",
                                "list"
                            )
                        )
                    ),
                    EditorJSDelimiter(
                        id = "SSIiQM8QW_",
                        data = Unit
                    ),
                    EditorJSQuote(
                        id = "3iWyZzWbmz",
                        data = EditorJSQuoteData(
                            text = "A quote<br>",
                            caption = "caption",
                            alignment = EditorJSQuoteAlignment.LEFT
                        )
                    ),
                    EditorJSWarning(
                        id = "QK8ZIRhemb",
                        data = EditorJSWarningData(
                            title = "WARNING",
                            message = "sample message"
                        )
                    ),
                    EditorJSCode(
                        id = "h9bxessZnC",
                        data = EditorJSCodeData(
                            code = "print(\"Hello World!)"
                        )
                    ),
                    EditorJSLatex(
                        id = "rgr3eg",
                        data = EditorJSLatexData(
                            math = "x^2 + 1 = 0 \\Rightarrow x = i"
                        )
                    )
                )
            )

            EditorJSContentComponent(content = sampleContent)
        }
    }

}