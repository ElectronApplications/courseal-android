package online.courseal.courseal_android.ui.screens.course

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollLessonType
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.LessonComponent
import online.courseal.courseal_android.ui.components.LessonType
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.course.CourseViewModel
import kotlin.math.min

@Composable
fun CourseStructureTab(
    modifier: Modifier = Modifier,
    onStartLesson: (lessonId: Int) -> Unit,
    onUnrecoverable: OnUnrecoverable,
    courseViewModel: CourseViewModel
) {
    val courseUiState by courseViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            val courseInfo = courseUiState.courseInfo

            if (courseInfo != null) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp),
                    text = courseInfo.courseName,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                courseInfo.lessons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .padding(top = 28.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        row.forEach { lesson ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(horizontal = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LessonComponent(
                                    onClick = {
                                        onStartLesson(lesson.lessonId)
                                    },
                                    enabled = lesson.canBeDone,
                                    progress = min(lesson.lessonProgress, lesson.lessonProgressNeeded).toFloat() / lesson.lessonProgressNeeded,
                                    lessonType = when (lesson.type) {
                                        CourseEnrollLessonType.LECTURE -> LessonType.LECTURE
                                        CourseEnrollLessonType.PRACTICE -> LessonType.PRACTICE
                                        CourseEnrollLessonType.TRAINING -> LessonType.TRAINING
                                        CourseEnrollLessonType.EXAM -> LessonType.EXAM
                                    }
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = lesson.lessonName,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}