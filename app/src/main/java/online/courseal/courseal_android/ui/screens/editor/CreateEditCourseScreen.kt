package online.courseal.courseal_android.ui.screens.editor

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.CoursealErrorButton
import online.courseal.courseal_android.ui.components.CoursealTextField
import online.courseal.courseal_android.ui.components.ErrorDialog
import online.courseal.courseal_android.ui.components.TopCancel
import online.courseal.courseal_android.ui.components.TopConfirm
import online.courseal.courseal_android.ui.components.adaptiveContainerWidth
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditCourseUiError
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditCourseViewModel
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

@Composable
fun CreateEditCourseScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel,
    createEditCourseViewModel: CreateEditCourseViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val createEditCourseUiState by createEditCourseViewModel.uiState.collectAsState()

    if (createEditCourseUiState.errorUnrecoverableState != null) {
        onUnrecoverable(createEditCourseUiState.errorUnrecoverableState!!)
    }

    ErrorDialog(
        isVisible = createEditCourseUiState.errorState != CreateEditCourseUiError.NONE,
        hideDialog = createEditCourseViewModel::hideError,
        title = when (createEditCourseUiState.errorState) {
            CreateEditCourseUiError.UNKNOWN -> stringResource(R.string.unknown_error)
            CreateEditCourseUiError.NONE -> ""
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TopCancel(onClick = onGoBack)
            TopConfirm(
                enabled = !createEditCourseUiState.makingRequest,
                onClick = {
                    coroutineScope.launch {
                        createEditCourseViewModel.confirm(onGoBack, editorViewModel)
                    }
                }
            )
        }

        if (createEditCourseUiState.loading) {
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
                    .adaptiveContainerWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp),
                    text = if (createEditCourseUiState.isCreating) stringResource(R.string.create_course) else
                        stringResource(R.string.edit_course),
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )

                CoursealTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp),
                    value = createEditCourseUiState.courseName,
                    onValueChange = createEditCourseViewModel::updateCourseName,
                    label = stringResource(R.string.course_name),
                    enabled = !createEditCourseUiState.makingRequest
                )

                CoursealTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp),
                    value = createEditCourseUiState.courseDescription,
                    onValueChange = createEditCourseViewModel::updateCourseDescription,
                    label = stringResource(R.string.course_description),
                    singleLine = false,
                    enabled = !createEditCourseUiState.makingRequest
                )

                if (!createEditCourseUiState.isCreating) {
                    CoursealErrorButton(
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 20.dp),
                        text = stringResource(R.string.delete_course),
                        onClick = {
                            coroutineScope.launch {
                                createEditCourseViewModel.delete(onGoBack, editorViewModel)
                            }
                        },
                        enabled = !createEditCourseUiState.makingRequest
                    )
                }
            }
        }
    }
}