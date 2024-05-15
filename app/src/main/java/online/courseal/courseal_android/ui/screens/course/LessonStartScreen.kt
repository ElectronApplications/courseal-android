package online.courseal.courseal_android.ui.screens.course

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskSingleExamAnswer
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskMultiple
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskSingle
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskMultiple
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskSingle
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealDropdownScreen
import online.courseal.courseal_android.ui.components.CoursealErrorButton
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealSelectableButton
import online.courseal.courseal_android.ui.components.CoursealSuccessButton
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopCancel
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.components.editorjs.content.EditorJSContentComponent
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette
import online.courseal.courseal_android.ui.viewmodels.course.CourseViewModel
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartUiContent
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartUiError
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartViewModel
import online.courseal.courseal_android.ui.viewmodels.course.LessonTask
import online.courseal.courseal_android.ui.viewmodels.course.TaskAnswer

@Composable
fun LessonStartScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    lessonStartViewModel: LessonStartViewModel = hiltViewModel(),
    courseViewModel: CourseViewModel
) {
    val lessonStartUiState by lessonStartViewModel.uiState.collectAsState()

    if (lessonStartUiState.errorUnrecoverableState != null) {
        onUnrecoverable(lessonStartUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = lessonStartUiState.errorState != LessonStartUiError.NONE,
        hideDialog = lessonStartViewModel::hideError,
        title = when (lessonStartUiState.errorState) {
            LessonStartUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            LessonStartUiError.NONE -> ""
        }
    )

    var warningShown by rememberSaveable { mutableStateOf(false) }
    BackHandler(
        enabled = !warningShown
    ) {
        warningShown = true
    }

    if (warningShown) {
        AlertDialog(
            onDismissRequest = { warningShown = false },
            dismissButton = {
                TextButton(onClick = { warningShown = false }) {
                    Text(stringResource(R.string.dismiss))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    warningShown = false
                    onGoBack()
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            title = { Text(stringResource(R.string.want_to_leave)) },
            text = { Text(stringResource(R.string.progress_lost)) }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopCancel(onClick = onGoBack)

                val progress = lessonStartUiState.tasksProgress
                if (progress != null) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, end = 15.dp, start = 5.dp),
                        progress = { progress }
                    )
                }
            }

            if (lessonStartUiState.loading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .adaptiveContainerWidth(800.dp)
                        .padding(top = 12.dp)
                ) {
                    when (val content = lessonStartUiState.currentContent) {
                        is LessonStartUiContent.LectureContent -> LessonLectureScreen(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            content = content.content,
                            lessonStartViewModel = lessonStartViewModel
                        )
                        is LessonStartUiContent.TaskContent -> {
                            when (content.task) {
                                is LessonTask.PracticeTrainingTask -> {
                                    when (content.task.content) {
                                        is CoursealTaskSingle -> LessonTaskSingleScreen(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally),
                                            content = TaskSingleAnswer.PracticeTrainingTask(content.task.content),
                                            lessonStartViewModel = lessonStartViewModel
                                        )
                                        is CoursealTaskMultiple -> LessonTaskMultipleScreen(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally),
                                            content = TaskMultipleAnswer.PracticeTrainingTask(content.task.content),
                                            lessonStartViewModel = lessonStartViewModel
                                        )
                                    }
                                }
                                is LessonTask.ExamTask -> {
                                    when (content.task.content) {
                                        is CoursealExamTaskSingle -> LessonTaskSingleScreen(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally),
                                            content = TaskSingleAnswer.ExamTask(content.task.content),
                                            lessonStartViewModel = lessonStartViewModel
                                        )
                                        is CoursealExamTaskMultiple -> LessonTaskMultipleScreen(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally),
                                            content = TaskMultipleAnswer.ExamTask(content.task.content),
                                            lessonStartViewModel = lessonStartViewModel
                                        )
                                    }
                                }
                            }
                        }
                        is LessonStartUiContent.ResultsContent -> {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.85f),
                                text = if (content.completed) stringResource(R.string.lesson_passed)
                                    else stringResource(R.string.lesson_not_passed),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.85f),
                                text = "XP ${content.xp}",
                            )

                            CoursealPrimaryButton(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.85f)
                                    .padding(top = 12.dp),
                                text = stringResource(R.string.confirm),
                                onClick = {
                                    courseViewModel.setNeedUpdate()
                                    onGoBack()
                                }
                            )
                        }
                        null -> {}
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = lessonStartUiState.taskCorrect != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {},
                color = Color.Black.copy(alpha = 0.5f),
                content = {}
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f, false))
            AnimatedVisibility(
                visible = lessonStartUiState.taskCorrect != null,
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (lessonStartUiState.taskCorrect == true) LocalCoursealPalette.current.successContainer
                    else LocalCoursealPalette.current.errorContainer,
                    contentColor = if (lessonStartUiState.taskCorrect == true) LocalCoursealPalette.current.onSuccess
                    else LocalCoursealPalette.current.onError
                ) {
                    if (lessonStartUiState.taskCorrect == true) {
                        CoursealSuccessButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f)
                                .padding(12.dp),
                            text = stringResource(R.string.correct),
                            onClick = {
                                lessonStartViewModel.hideTaskCorrect()
                            }
                        )
                    } else if (lessonStartUiState.taskCorrect == false) {
                        CoursealErrorButton(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f)
                                .padding(24.dp),
                            text = stringResource(R.string.wrong_answer),
                            onClick = {
                                lessonStartViewModel.hideTaskCorrect()
                            }
                        )
                    }
                }
            }
        }
    }
}