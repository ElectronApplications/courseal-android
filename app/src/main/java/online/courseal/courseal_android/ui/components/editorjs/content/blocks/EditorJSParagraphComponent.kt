package online.courseal.courseal_android.ui.components.editorjs.content.blocks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSParagraphData
import online.courseal.courseal_android.ui.components.TextHTML

@Composable
fun EditorJSParagraphComponent(
    modifier: Modifier = Modifier,
    data: EditorJSParagraphData
) {
    TextHTML(
        modifier = modifier,
        text = data.text,
        style = MaterialTheme.typography.bodyLarge
    )
}