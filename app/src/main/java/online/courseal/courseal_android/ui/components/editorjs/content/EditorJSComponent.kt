package online.courseal.courseal_android.ui.components.editorjs.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
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
    SelectionContainer {
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
}

@Composable
fun EditorJSBlockComponent(
    modifier: Modifier = Modifier,
    block: EditorJSBlock
) {
    when(block) {
        is EditorJSCode -> EditorJSCodeComponent(modifier = modifier, data = block.data)
        is EditorJSDelimiter -> EditorJSDelimiterComponent(modifier = modifier)
        is EditorJSHeader -> EditorJSHeaderComponent(modifier = modifier, data = block.data)
        is EditorJSImage -> EditorJSImageComponent(modifier = modifier, data = block.data)
        is EditorJSList -> EditorJSListComponent(modifier = modifier, data = block.data)
        is EditorJSParagraph -> EditorJSParagraphComponent(modifier = modifier, data = block.data)
        is EditorJSQuote -> EditorJSQuoteComponent(modifier = modifier, data = block.data)
        is EditorJSWarning -> EditorJSWarningComponent(modifier = modifier, data = block.data)
        is EditorJSLatex -> EditorJSLatexComponent(modifier = modifier, data = block.data)
    }
}