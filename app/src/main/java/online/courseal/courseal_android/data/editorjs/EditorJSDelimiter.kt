package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("delimiter")
data class EditorJSDelimiter(
    override val id: String,
    val data: Unit
) : EditorJSBlock()