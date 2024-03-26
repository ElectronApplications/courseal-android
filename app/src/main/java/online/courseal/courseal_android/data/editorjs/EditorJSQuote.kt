package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("quote")
class EditorJSQuote (
    override val id: String,
    val data: EditorJSQuoteData
) : EditorJSBlock()

@Serializable
data class EditorJSQuoteData(
    val text: String,
    val caption: String,
    val alignment: EditorJSQuoteAlignment
)

enum class EditorJSQuoteAlignment(val alignment: String) {
    LEFT("left"),
    RIGHT("right")
}