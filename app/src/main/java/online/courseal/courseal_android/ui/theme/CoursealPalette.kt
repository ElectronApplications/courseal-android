package online.courseal.courseal_android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CoursealPalette(
    val welcomeGradientBottom: Color = Color.Unspecified,
    val welcomeGradientTop: Color = Color.Unspecified,
    val onWelcomeGradient: Color = Color.Unspecified,
)

val LocalCoursealPalette = staticCompositionLocalOf { CoursealPalette() }