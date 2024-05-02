package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonExam
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonLecture
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonPractice
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonTraining
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.LessonComponent
import online.courseal.courseal_android.ui.components.LessonType
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

@Composable
fun EditorStructureTab(
    modifier: Modifier = Modifier,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel
) {
    val editorUiState by editorViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            val structure = editorUiState.courseStructure
            val lessons = editorUiState.courseLessons

            if (structure != null && lessons != null) {
                structure.toMutableList().apply { add(emptyList()) }.forEach { row ->
                    Row(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { lesson ->
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                val lessonData = lessons.find { it.lessonId == lesson.lessonId }
                                LessonComponent(
                                    onClick = { /* TODO */ },
                                    lessonType = when (lessonData?.lesson) {
                                        is CoursealLessonLecture -> LessonType.LECTURE
                                        is CoursealLessonPractice -> LessonType.PRACTICE
                                        is CoursealLessonTraining -> LessonType.TRAINING
                                        is CoursealLessonExam -> LessonType.EXAM
                                        null -> LessonType.LECTURE
                                    }
                                )
                                Text(
                                    modifier = Modifier
                                        .padding(top = 6.dp),
                                    text = lessonData?.lessonName ?: "",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        if (row.size < 3) {
                            OutlinedIconButton(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .padding(bottom = 22.dp),
                                onClick = { /* TODO */ }
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = stringResource(R.string.add_lesson))
                            }
                        }
                    }
                }
           }
       }
    }
}