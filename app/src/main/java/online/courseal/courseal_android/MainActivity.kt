package online.courseal.courseal_android

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import online.courseal.courseal_android.ui.theme.CoursealTheme

typealias SetStatusBarColor = (color: Color, isTextDark: Boolean) -> Unit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursealTheme {
                val view = LocalView.current
                val window = (view.context as Activity).window
                val setStatusBarColor: SetStatusBarColor = { color, isTextDark ->
                    if (!view.isInEditMode) {
                        window.statusBarColor = color.toArgb()
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = isTextDark
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainApp(
                        setStatusBarColor = setStatusBarColor
                    )
                }
            }
        }
    }
}