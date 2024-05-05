package online.courseal.courseal_android.ui.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

enum class LessonType(val nameId: Int) {
    LECTURE(R.string.lecture, ),
    PRACTICE(R.string.practice),
    TRAINING(R.string.training),
    EXAM(R.string.exam)
}

@Composable
fun LessonComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    lessonType: LessonType,
    enabled: Boolean = true,
    @FloatRange(from = 0.0, to = 1.0) progress: Float = 1.0f
) {
    Box(
        modifier = modifier
            .width(72.dp)
            .height(72.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .clip(CircleShape)
                .let { if (enabled) it.clickable(onClick = onClick) else it },
            color = MaterialTheme.colorScheme.background
        ) {
            Image(
                modifier = Modifier
                    .let {
                        if (enabled)
                            it.background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        LocalCoursealPalette.current.welcomeGradientTop,
                                        LocalCoursealPalette.current.welcomeGradientBottom
                                    )
                                )
                            )
                        else
                            it.background(MaterialTheme.colorScheme.surface)
                    }
                    .padding(12.dp)
                    .width(36.dp),
                contentScale = ContentScale.FillWidth,
                painter = when (lessonType) {
                    LessonType.LECTURE -> painterResource(R.drawable.baseline_menu_book_24)
                    LessonType.PRACTICE -> painterResource(R.drawable.baseline_target_24)
                    LessonType.TRAINING -> painterResource(R.drawable.baseline_exercise_24)
                    LessonType.EXAM -> painterResource(R.drawable.baseline_edit_note_24)
                },
                contentDescription = stringResource(lessonType.nameId),
                colorFilter = ColorFilter.tint(LocalCoursealPalette.current.onWelcomeGradient)
            )
        }

        val arcColor = if (enabled) LocalCoursealPalette.current.welcomeGradientBottom
            else MaterialTheme.colorScheme.surface

        val arcWidth = LocalDensity.current.run { 6.dp.toPx() }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawArc(
                color = arcColor,
                startAngle = -90.0f,
                sweepAngle = 360.0f * progress,
                useCenter = false,
                style = Stroke(arcWidth, cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
    }
}