package online.courseal.courseal_android.ui.components.editorjs.content.blocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSWarningData
import online.courseal.courseal_android.ui.components.TextHTML
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun EditorJSWarningComponent(
    modifier: Modifier = Modifier,
    data: EditorJSWarningData
) {
    Row(
        modifier = modifier
            .background(LocalCoursealPalette.current.warning)
            .padding(vertical = 8.dp)
    ) {
        Image(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .width(32.dp)
                .align(Alignment.CenterVertically)
            ,
            contentScale = ContentScale.FillWidth,
            painter = rememberVectorPainter(image = Icons.Outlined.Warning),
            contentDescription = stringResource(R.string.warning)
        )
        Column {
            TextHTML(
                text = data.title,
                style = MaterialTheme.typography.headlineSmall,
                color = LocalCoursealPalette.current.onWarning
            )
            TextHTML(
                text = data.message,
                style = MaterialTheme.typography.bodyLarge,
                color = LocalCoursealPalette.current.onWarning
            )
        }

    }
}