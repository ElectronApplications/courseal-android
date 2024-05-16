package online.courseal.courseal_android.ui.components.editorjs.content.blocks

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeaderData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeaderLevel
import online.courseal.courseal_android.ui.components.TextHTML

@Composable
fun EditorJSHeaderComponent(
    modifier: Modifier = Modifier,
    data: EditorJSHeaderData
) {
    TextHTML(
        modifier = modifier,
        text = data.text,
        style = when(data.level) {
            EditorJSHeaderLevel.H1 -> MaterialTheme.typography.headlineMedium
            EditorJSHeaderLevel.H2 -> MaterialTheme.typography.headlineMedium
            EditorJSHeaderLevel.H3 -> MaterialTheme.typography.headlineSmall
            EditorJSHeaderLevel.H4 -> MaterialTheme.typography.headlineSmall
            EditorJSHeaderLevel.H5 -> MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            EditorJSHeaderLevel.H6 -> MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        }
    )
}