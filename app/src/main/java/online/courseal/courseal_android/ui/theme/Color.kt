package online.courseal.courseal_android.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0066FF),
    secondary = Color(0xFFD3EAFF),
    tertiary = Color(0xFFEEF0F7),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFF2F2F2),

    onPrimary = Color.White,
    onSecondary = Color(0xFF0969DA),
    onTertiary = Color(0xFF525F7F),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

val LightCoursealPalette = CoursealPalette(
    welcomeGradientTop = Color(0xFFD4E3EB),
    welcomeGradientBottom = Color(0xFFCFC69D),
    onWelcomeGradient = Color(0xFF35415F),
    link = Color(0xFF0066FF),
    warning = Color(0xFFFAECC6),
    onWarning = Color(0xFF3B4045)
)


val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3570EB),
    secondary = Color(0xFF202F36),
    tertiary = Color(0xFF3F3F3F),
    background = Color(0xFF242424),
    surface = Color(0xFF3A3A3A),

    onPrimary = Color.White,
    onSecondary = Color(0xFF49BFF6),
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

val DarkCoursealPalette = CoursealPalette(
    welcomeGradientTop = Color(0xFF33444D),
    welcomeGradientBottom = Color(0xFF78735B),
    onWelcomeGradient = Color.White,
    link = Color(0xFF59A6FF),
    warning = Color(0xFFFAECC6),
    onWarning = Color(0xFF3B4045)
)