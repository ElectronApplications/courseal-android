package online.courseal.courseal_android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun CoursealTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val (colorScheme, coursealPalette) = when {
        darkTheme -> Pair(DarkColorScheme, DarkCoursealPalette)
        else -> Pair(LightColorScheme, LightCoursealPalette)
    }

    CompositionLocalProvider(
        LocalCoursealPalette provides coursealPalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}