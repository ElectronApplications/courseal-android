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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskMultiple
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskSingle
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
import online.courseal.courseal_android.ui.viewmodels.editor.CoursealTaskType
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditTaskUiError
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditTaskViewModel
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditTaskScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onEditTaskBody: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel,
    createEditTaskViewModel: CreateEditTaskViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val createEditTaskUiState by createEditTaskViewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        createEditTaskViewModel.passEditorViewModel(editorViewModel)
    }

    if (createEditTaskUiState.errorUnrecoverableState != null) {
        onUnrecoverable(createEditTaskUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = createEditTaskUiState.errorState != CreateEditTaskUiError.NONE,
        hideDialog = createEditTaskViewModel::hideError,
        title = when (createEditTaskUiState.errorState) {
            CreateEditTaskUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            CreateEditTaskUiError.NONE -> ""
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
                enabled = !createEditTaskUiState.makingRequest,
                onClick = {
                    coroutineScope.launch {
                        createEditTaskViewModel.confirm(onGoBack = onGoBack)
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
                text = if (createEditTaskUiState.isCreating) stringResource(R.string.create_task) else
                    stringResource(R.string.edit_task),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )

            CoursealTextField(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp),
                value = createEditTaskUiState.taskName,
                onValueChange = createEditTaskViewModel::updateTaskName,
                label = stringResource(R.string.task_name),
                enabled = !createEditTaskUiState.makingRequest
            )

            CoursealSecondaryButton(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp),
                text = stringResource(R.string.edit_task_body),
                onClick = onEditTaskBody
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
                    value = when (createEditTaskUiState.task) {
                        is CoursealTaskSingle -> stringResource(R.string.single_option)
                        is CoursealTaskMultiple -> stringResource(R.string.multiple_option)
                    },
                    onValueChange = {},
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = typeDropdownExpanded
                        )
                    },
                )

                ExposedDropdownMenu(expanded = typeDropdownExpanded, onDismissRequest = { typeDropdownExpanded = false }) {
                    CoursealTaskType.entries.forEach { entry ->
                        DropdownMenuItem(
                            text = { Text(stringResource(entry.nameId)) },
                            onClick = {
                                typeDropdownExpanded = false
                                createEditTaskViewModel.updateTaskType(entry)
                            }
                        )
                    }
                }
            }

            CoursealOutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp)
            ) {
                createEditTaskUiState.options.forEachIndexed { index, option ->
                    CoursealOutlinedCardItem(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextField(
                            modifier = Modifier
                                .weight(1f, fill = false),
                            value = option.first,
                            onValueChange = {
                                createEditTaskViewModel.updateOptionText(index, it)
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background
                            )
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = option.second,
                                onCheckedChange = {
                                    createEditTaskViewModel.checkOption(index)
                                }
                            )
                            Icon(
                                modifier = Modifier
                                    .scale(1.25f)
                                    .clickable {
                                        createEditTaskViewModel.removeOption(index)
                                    },
                                imageVector = Icons.Filled.Clear,
                                contentDescription = stringResource(R.string.delete_option),
                            )
                        }
                    }
                }
            }

            CoursealSecondaryButton(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp),
                text = stringResource(R.string.add_option),
                onClick = {
                    createEditTaskViewModel.addOption()
                }
            )

            if (!createEditTaskUiState.isCreating) {
                CoursealErrorButton(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 32.dp),
                    text = stringResource(R.string.delete_task),
                    onClick = {
                        coroutineScope.launch {
                            createEditTaskViewModel.delete(onGoBack)
                        }
                    },
                    enabled = !createEditTaskUiState.makingRequest
                )
            }
        }
    }
}