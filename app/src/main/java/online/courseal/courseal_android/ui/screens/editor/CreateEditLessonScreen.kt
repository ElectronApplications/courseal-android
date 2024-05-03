package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonExam
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonLecture
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonPractice
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonTraining
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealErrorButton
import online.courseal.courseal_android.ui.components.CoursealOutlinedCard
import online.courseal.courseal_android.ui.components.CoursealOutlinedCardItem
import online.courseal.courseal_android.ui.components.CoursealSecondaryButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopCancel
import online.courseal.courseal_android.ui.components.TopConfirm
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.editor.CoursealLessonType
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditLessonUiError
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditLessonViewModel
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditLessonScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onEditLectureContent: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel,
    createEditLessonViewModel: CreateEditLessonViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val createEditLessonUiState by createEditLessonViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        createEditLessonViewModel.passEditorViewModel(editorViewModel)
    }

    if (createEditLessonUiState.errorUnrecoverableState != null) {
        onUnrecoverable(createEditLessonUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = createEditLessonUiState.errorState != CreateEditLessonUiError.NONE,
        hideDialog = createEditLessonViewModel::hideError,
        title = when (createEditLessonUiState.errorState) {
            CreateEditLessonUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            CreateEditLessonUiError.NONE -> ""
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopCancel(onClick = onGoBack)
            TopConfirm(
                enabled = !createEditLessonUiState.makingRequest && (createEditLessonUiState.tasks?.size ?: 4) >= 4,
                onClick = {
                    coroutineScope.launch {
                        createEditLessonViewModel.confirm(onGoBack = onGoBack)
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .adaptiveContainerWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 10.dp),
                text = if (createEditLessonUiState.isCreating) stringResource(R.string.create_lesson) else
                    stringResource(R.string.edit_lesson),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )

            CoursealTextField(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp),
                value = createEditLessonUiState.lessonName,
                onValueChange = createEditLessonViewModel::updateLessonName,
                label = stringResource(R.string.lesson_name),
                enabled = !createEditLessonUiState.makingRequest
            )

            var typeDropdownExpanded by rememberSaveable { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                expanded = typeDropdownExpanded,
                onExpandedChange = {
                    typeDropdownExpanded = !typeDropdownExpanded
                }
            ) {
                CoursealTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp)
                        .menuAnchor(),
                    readOnly = true,
                    value = when (createEditLessonUiState.lesson) {
                        is CoursealLessonLecture -> stringResource(R.string.lecture)
                        is CoursealLessonPractice -> stringResource(R.string.practice)
                        is CoursealLessonTraining -> stringResource(R.string.training)
                        is CoursealLessonExam -> stringResource(R.string.exam)
                    },
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = typeDropdownExpanded
                        )
                    },
                )

                ExposedDropdownMenu(expanded = typeDropdownExpanded, onDismissRequest = { typeDropdownExpanded = false }) {
                    CoursealLessonType.entries.forEach { entry ->
                        DropdownMenuItem(
                            text = { Text(stringResource(entry.nameId)) },
                            onClick = {
                                typeDropdownExpanded = false
                                createEditLessonViewModel.updateLessonType(entry)
                            }
                        )
                    }
                }
            }

            if (createEditLessonUiState.lesson !is CoursealLessonLecture) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp),
                    text = "${stringResource(R.string.progress_needed)} (${createEditLessonUiState.progressNeeded})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Slider(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally),
                    value = createEditLessonUiState.progressNeeded.toFloat(),
                    onValueChange = { createEditLessonViewModel.updateProgressNeeded(it.roundToInt()) },
                    steps = 6,
                    valueRange = 1.0f..6.0f
                )
            }

            val tasks = createEditLessonUiState.tasks
            if (createEditLessonUiState.lesson is CoursealLessonLecture) {
                CoursealSecondaryButton(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 12.dp),
                    text = stringResource(R.string.edit_lecture_content),
                    onClick = onEditLectureContent
                )
            } else if (tasks != null) {
                CoursealOutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                ) {
                    tasks.forEachIndexed { index, taskName ->
                        CoursealOutlinedCardItem(
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f, fill = false),
                                text = taskName,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                modifier = Modifier
                                    .scale(1.25f)
                                    .clickable {
                                        createEditLessonViewModel.removeTask(index)
                                    },
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(R.string.remove_task),
                            )
                        }
                    }
                }

                var tasksDropdownExpanded by rememberSaveable { mutableStateOf(false) }

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                ) {
                    CoursealSecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp),
                        text = stringResource(R.string.add_task),
                        onClick = { tasksDropdownExpanded = true }
                    )

                    DropdownMenu(
                        expanded = tasksDropdownExpanded,
                        onDismissRequest = { tasksDropdownExpanded = false }
                    ) {
                        createEditLessonUiState.availableTasks!!.forEach { (taskName, taskId) ->
                            DropdownMenuItem(
                                text = { Text(taskName) },
                                onClick = {
                                    tasksDropdownExpanded = false
                                    createEditLessonViewModel.addTask(taskId)
                                }
                            )
                        }
                    }
                }
            }

            if (!createEditLessonUiState.isCreating) {
                CoursealErrorButton(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp),
                    text = stringResource(R.string.delete_lesson),
                    onClick = {
                        coroutineScope.launch {
                            createEditLessonViewModel.delete(onGoBack)
                        }
                    },
                    enabled = !createEditLessonUiState.makingRequest
                )
            }
        }
    }
}