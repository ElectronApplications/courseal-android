package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("code")
data class EditorJSCode (
    override val id: String,
    val data: EditorJSCodeData
) : EditorJSBlock()

@Serializable
data class EditorJSCodeData(
    val code: String
)