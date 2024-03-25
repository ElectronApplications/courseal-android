package online.courseal.courseal_android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0066FF),
    secondary = Color(0xFF121D2F),
    tertiary = Color(0xFF3F3F3F),
    background = Color(0xFF242424),
    surface = Color(0xFF3A3A3A),

    onPrimary = Color.White,
    onSecondary = Color(0xFF4493F8),
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0066FF),
    secondary = Color(0xFFDDF4FF),
    tertiary = Color(0xFFEEF0F7),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFF2F2F2),

    onPrimary = Color.White,
    onSecondary = Color(0xFF0969DA),
    onTertiary = Color(0xFF525F7F),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF2D2D2D),
)

private val DarkCoursealPalette = CoursealPalette(
    welcomeGradientTop = DarkTopGradient,
    welcomeGradientBottom = DarkBottomGradient,
    onWelcomeGradient = DarkGradientText
)

private val LightCoursealPalette = CoursealPalette(
    welcomeGradientTop = LightTopGradient,
    welcomeGradientBottom = LightBottomGradient,
    onWelcomeGradient = LightGradientText
)

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