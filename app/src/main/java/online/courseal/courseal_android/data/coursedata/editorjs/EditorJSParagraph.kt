package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("paragraph")
data class EditorJSParagraph(
    override val id: String,
    val data: EditorJSParagraphData
) : EditorJSBlock()

@Serializable
data class EditorJSParagraphData(
    val text: String
)