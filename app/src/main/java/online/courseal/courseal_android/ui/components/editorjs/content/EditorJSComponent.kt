package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.courseal.courseal_android.data.editorjs.EditorJSBlock
import online.courseal.courseal_android.data.editorjs.EditorJSCode
import online.courseal.courseal_android.data.editorjs.EditorJSContent
import online.courseal.courseal_android.data.editorjs.EditorJSDelimiter
import online.courseal.courseal_android.data.editorjs.EditorJSHeader
import online.courseal.courseal_android.data.editorjs.EditorJSImage
import online.courseal.courseal_android.data.editorjs.EditorJSLatex
import online.courseal.courseal_android.data.editorjs.EditorJSList
import online.courseal.courseal_android.data.editorjs.EditorJSParagraph
import online.courseal.courseal_android.data.editorjs.EditorJSQuote
import online.courseal.courseal_android.data.editorjs.EditorJSWarning

@Composable
fun EditorJSContentComponent(
    modifier: Modifier = Modifier,
    content: EditorJSContent
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.safeDrawing))
        for (block in content.blocks) {
            EditorJSBlockComponent(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                block = block
            )
        }
    }
}

@Composable
fun EditorJSBlockComponent(
    modifier: Modifier = Modifier,
    block: EditorJSBlock
) {
    when(block) {
        is EditorJSCode -> {}
        is EditorJSDelimiter -> EditorJSDelimiterComponent(modifier = modifier)
        is EditorJSHeader -> EditorJSHeaderComponent(modifier = modifier, data = block.data)
        is EditorJSImage -> {}
        is EditorJSList -> {}
        is EditorJSParagraph -> EditorJSParagraphComponent(modifier = modifier, data = block.data)
        is EditorJSQuote -> {}
        is EditorJSWarning -> {}
        is EditorJSLatex -> EditorJSLatexComponent(modifier = modifier, data = block.data)
    }
}