package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("delimiter")
class EditorJSDelimiter(
    override val id: String,
    val data: Nothing
) : EditorJSBlock()