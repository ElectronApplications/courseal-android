package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSImageData
import online.courseal.courseal_android.ui.components.TextHTML

@Composable
fun EditorJSImageComponent(
    modifier: Modifier = Modifier,
    data: EditorJSImageData
) {
    Column(
        modifier = modifier
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            contentScale = if (data.stretched)
                ContentScale.FillWidth else ContentScale.Fit,
            model = data.file.url,
            contentDescription = data.caption
        )
        TextHTML(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = data.caption,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}