package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSListData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSListStyle
import online.courseal.courseal_android.ui.components.TextHTML

@Composable
fun EditorJSListComponent(
    modifier: Modifier = Modifier,
    data: EditorJSListData
) {
    Column(
        modifier = modifier
    ) {
        data.items.forEachIndexed { index, element ->
            Row {
                Text(
                    text = if (data.style == EditorJSListStyle.UNORDERED) "\u2022 "
                    else "${index + 1}) "
                )

                TextHTML(
                    text = element,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}