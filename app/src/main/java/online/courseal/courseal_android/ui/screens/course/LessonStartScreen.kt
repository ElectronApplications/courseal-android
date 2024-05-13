package online.courseal.courseal_android.ui.screens.course

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskMultiple
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskSingle
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.CoursealSelectableButton
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopCancel
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.components.editorjs.content.EditorJSContentComponent
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartUiContent
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartUiError
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartViewModel

@Composable
fun LessonStartScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    lessonStartViewModel: LessonStartViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
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
                    Text("Dismiss")
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TopCancel(onClick = onGoBack)

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
                    is LessonStartUiContent.LectureContent -> {
                        EditorJSContentComponent(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            content = content.content
                        )
                    }
                    is LessonStartUiContent.TaskContent -> {
                        EditorJSContentComponent(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            content = when (content.content) {
                                is CoursealExamTaskMultiple -> content.content.body
                                is CoursealExamTaskSingle -> content.content.body
                            }
                        )

                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85f),
                            fontWeight = FontWeight.SemiBold,
                            text = when (content.content) {
                                is CoursealExamTaskSingle -> "Choose the correct answer"
                                is CoursealExamTaskMultiple -> "Choose all correct answers"
                            }
                        )

                        val options = when(content.content) {
                            is CoursealExamTaskSingle -> content.content.options
                            is CoursealExamTaskMultiple -> content.content.options.map { it.text }
                        }

                        options.forEachIndexed { index, option ->
                            CoursealSelectableButton(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth(0.85f),
                                text = option,
                                selected = index in lessonStartUiState.selectedOptions,
                                onClick = {
                                    lessonStartViewModel.selectOption(index)
                                }
                            )
                        }
                    }
                    is LessonStartUiContent.ResultsContent -> {}
                    null -> {}
                }

                CoursealPrimaryButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.85f)
                        .padding(top = 12.dp),
                    text = stringResource(R.string.confirm),
                    enabled = lessonStartUiState.confirmEnabled,
                    onClick = {
                        coroutineScope.launch {
                            lessonStartViewModel.confirmAnswer()
                        }
                    }
                )
            }
        }
    }
}