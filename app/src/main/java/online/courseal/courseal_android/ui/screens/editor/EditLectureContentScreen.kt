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
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonLecture
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.components.TopBack
import online.courseal.courseal_android.ui.components.editorjs.editor.EditorJSEditorComponent
import online.courseal.courseal_android.ui.viewmodels.editor.CreateEditLessonViewModel

@Composable
fun EditLectureContentScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    createEditLessonViewModel: CreateEditLessonViewModel
) {
    val createEditLessonUiState by createEditLessonViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        TopBack(onClick = onGoBack)

        EditorJSEditorComponent(
            modifier = Modifier
                .fillMaxSize(),
            data = (createEditLessonUiState.lesson as CoursealLessonLecture).lectureContent,
            onDataChange = createEditLessonViewModel::updateLectureContent,
            saveEndpoint = createEditLessonUiState.saveEndpoint
        )
    }
}