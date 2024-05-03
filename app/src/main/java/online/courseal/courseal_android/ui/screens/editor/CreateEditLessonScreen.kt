package online.courseal.courseal_android.ui.screens.editor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.viewmodels.editor.EditorViewModel

@Composable
fun CreateEditLessonScreen(
    modifier: Modifier = Modifier,
    onGoBack: () -> Unit,
    onUnrecoverable: OnUnrecoverable,
    editorViewModel: EditorViewModel
) {
    val coroutineScope = rememberCoroutineScope()

}