package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun EditorJSDelimiterComponent(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        text = "* * *",
        style = MaterialTheme.typography.headlineLarge
    )
}