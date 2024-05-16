package online.courseal.courseal_android.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CoursealPalette(
    val welcomeGradientBottom: Color = Color.Unspecified,
    val welcomeGradientTop: Color = Color.Unspecified,
    val onWelcomeGradient: Color = Color.Unspecified,

    val topButtonTextColor: Color = Color.Unspecified,
    val topButtonTextColorDisabled: Color = Color.Unspecified,
    val link: Color = Color.Unspecified,

    val warning: Color = Color.Unspecified,
    val onWarning: Color = Color.Unspecified,

    val successContainer: Color = Color.Unspecified,
    val success: Color = Color.Unspecified,
    val onSuccess: Color = Color.Unspecified,

    val errorContainer: Color = Color.Unspecified,
    val error: Color = Color.Unspecified,
    val onError: Color = Color.Unspecified,

    val activityPalette: List<Color> = emptyList() // Must be 4 items
)

val LocalCoursealPalette = staticCompositionLocalOf { CoursealPalette() }