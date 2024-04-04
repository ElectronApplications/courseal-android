package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.data.editorjs.EditorJSCodeData
import online.courseal.courseal_android.ui.theme.CodeStyle

@Composable
fun EditorJSCodeComponent(
    modifier: Modifier = Modifier,
    data: EditorJSCodeData
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(all = 10.dp)
                .horizontalScroll(rememberScrollState()),
            text = data.code,
            style = CodeStyle
        )
    }
}