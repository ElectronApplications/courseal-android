package online.courseal.courseal_android.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightTopGradient = Color(0xFFD4E3EB)
val LightBottomGradient = Color(0xFFCFC69D)
val LightGradientText = Color(0xFF35415F)

val DarkTopGradient = Color(0xFF33444D)
val DarkBottomGradient = Color(0xFF78735B)
val DarkGradientText = Color.White


val DarkColorScheme = darkColorScheme(
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

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0066FF),
    secondary = Color(0xFFDDF4FF),
    tertiary = Color(0xFFEEF0F7),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFF2F2F2),

    onPrimary = Color.White,
    onSecondary = Color(0xFF0969DA),
    onTertiary = Color(0xFF525F7F),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

val DarkCoursealPalette = CoursealPalette(
    welcomeGradientTop = DarkTopGradient,
    welcomeGradientBottom = DarkBottomGradient,
    onWelcomeGradient = DarkGradientText
)

val LightCoursealPalette = CoursealPalette(
    welcomeGradientTop = LightTopGradient,
    welcomeGradientBottom = LightBottomGradient,
    onWelcomeGradient = LightGradientText
)