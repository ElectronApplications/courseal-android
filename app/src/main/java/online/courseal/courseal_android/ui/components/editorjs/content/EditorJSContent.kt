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
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSBlock
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSCode
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSDelimiter
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSEmbed
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeader
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSImage
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSLatex
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSList
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSParagraph
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSQuote
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSWarning
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSCodeComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSDelimiterComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSHeaderComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSImageComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSLatexComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSListComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSParagraphComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSQuoteComponent
import online.courseal.courseal_android.ui.components.editorjs.content.blocks.EditorJSWarningComponent

@Composable
fun EditorJSContentComponent(
    modifier: Modifier = Modifier,
    content: EditorJSContent
) {
    SelectionContainer(
        modifier = modifier
    ) {
        Column {
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
        is EditorJSEmbed -> { /* TODO */ }
    }
}