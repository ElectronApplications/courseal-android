package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.ui.OnUnrecoverable
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
            .fillMaxSize()
    ) {
        Text(editorUiState.courseInfo?.courseName ?: "")
    }
}