package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("header")
class EditorJSHeader (
    override val id: String,
    val data: EditorJSHeaderData
) : EditorJSBlock()

@Serializable
data class EditorJSHeaderData(
    val text: String,
    val level: Int
)