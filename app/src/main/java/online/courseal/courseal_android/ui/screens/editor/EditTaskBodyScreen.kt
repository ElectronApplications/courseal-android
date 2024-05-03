package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskMultiple
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskSingle
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.TopBack
import online.courseal.courseal_android.ui.components.editorjs.editor.EditorJSEditorComponent
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditTaskViewModel

@Composable
fun EditTaskBodyScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    createEditTaskViewModel: CreateEditTaskViewModel
) {
    val createEditTaskUiState by createEditTaskViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TopBack(onClick = onGoBack)

        EditorJSEditorComponent(
            modifier = Modifier
                .fillMaxSize(),
            data = when (val currentTask = createEditTaskUiState.task) {
                is CoursealTaskMultiple -> currentTask.body
                is CoursealTaskSingle -> currentTask.body
            },
            onDataChange = createEditTaskViewModel::updateTaskBody,
            saveEndpoint = createEditTaskUiState.saveEndpoint
        )
    }
}