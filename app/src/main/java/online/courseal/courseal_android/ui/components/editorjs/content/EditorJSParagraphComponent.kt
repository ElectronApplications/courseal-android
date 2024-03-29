package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.data.editorjs.EditorJSParagraphData

@Composable
fun EditorJSParagraphComponent(
    modifier: Modifier = Modifier,
    data: EditorJSParagraphData
) {
    Text(
        modifier = modifier,
        text = data.text,
        style = MaterialTheme.typography.bodyLarge
    )
}