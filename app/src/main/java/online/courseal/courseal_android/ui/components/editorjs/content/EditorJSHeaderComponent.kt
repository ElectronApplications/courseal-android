package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.data.editorjs.EditorJSHeaderData
import online.courseal.courseal_android.data.editorjs.EditorJSHeaderLevel

@Composable
fun EditorJSHeaderComponent(
    modifier: Modifier = Modifier,
    data: EditorJSHeaderData
) {
    Text(
        modifier = modifier,
        text = data.text,
        style = when(data.level) {
            EditorJSHeaderLevel.H1 -> MaterialTheme.typography.headlineLarge
            EditorJSHeaderLevel.H2 -> MaterialTheme.typography.headlineLarge
            EditorJSHeaderLevel.H3 -> MaterialTheme.typography.headlineMedium
            EditorJSHeaderLevel.H4 -> MaterialTheme.typography.headlineMedium
            EditorJSHeaderLevel.H5 -> MaterialTheme.typography.headlineSmall
            EditorJSHeaderLevel.H6 -> MaterialTheme.typography.headlineSmall
        }
    )
}