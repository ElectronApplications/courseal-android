package online.courseal.courseal_android

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoursealTheme {
                var statusBarColor by rememberSaveable { mutableIntStateOf(0) }
                var statusBarTextDark by rememberSaveable { mutableStateOf(true) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp { color, isTextDark ->
                        statusBarColor = color.toArgb()
                        statusBarTextDark = isTextDark
                    }
                }

                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val window = (view.context as Activity).window
                        window.statusBarColor = statusBarColor
                        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = statusBarTextDark
                    }
                }
            }
        }
    }
}

typealias SetStatusBarColor = (color: Color, isTextDark: Boolean) -> Unit