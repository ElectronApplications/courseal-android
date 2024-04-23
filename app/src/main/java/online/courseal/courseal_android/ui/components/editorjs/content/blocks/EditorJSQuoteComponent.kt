package online.courseal.courseal_android.ui.components.editorjs.content.blocks

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSQuoteAlignment
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSQuoteData
import online.courseal.courseal_android.ui.components.TextHTML

@Composable
fun EditorJSQuoteComponent(
    modifier: Modifier = Modifier,
    data: EditorJSQuoteData
) {
    Column(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .width(48.dp)
                .rotate(180f),
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = R.drawable.baseline_format_quote_24),
            contentDescription = "\""
        )

        TextHTML(
            modifier = Modifier
                .fillMaxWidth(),
            text = data.text,
            style = MaterialTheme.typography.headlineSmall.copy(
                textAlign = if (data.alignment == EditorJSQuoteAlignment.LEFT)
                    TextAlign.Left else TextAlign.Right
            )
        )

        TextHTML(
            modifier = Modifier
                .fillMaxWidth(),
            text = "- ${data.caption}",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = if (data.alignment == EditorJSQuoteAlignment.LEFT)
                    TextAlign.Left else TextAlign.Right
            )
        )
    }
}