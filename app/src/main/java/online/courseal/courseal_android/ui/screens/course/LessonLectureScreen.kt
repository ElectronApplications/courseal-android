package online.courseal.courseal_android.ui.screens.course

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent
import online.courseal.courseal_android.ui.components.CoursealPrimaryButton
import online.courseal.courseal_android.ui.components.editorjs.content.EditorJSContentComponent
import online.courseal.courseal_android.ui.viewmodels.course.LessonStartViewModel

@Composable
fun LessonLectureScreen(
    modifier: Modifier = Modifier,
    content: EditorJSContent,
    lessonStartViewModel: LessonStartViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        EditorJSContentComponent(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.85f),
            content = content
        )

        CoursealPrimaryButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.85f)
                .padding(top = 12.dp),
            text = stringResource(R.string.confirm),
            onClick = {
                coroutineScope.launch {
                    lessonStartViewModel.finishLesson()
                }
            }
        )
    }
}