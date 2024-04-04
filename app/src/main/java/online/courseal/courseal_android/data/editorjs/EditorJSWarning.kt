package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("warning")
data class EditorJSWarning(
    override val id: String,
    val data: EditorJSWarningData
) : EditorJSBlock()

@Serializable
data class EditorJSWarningData(
    val title: String,
    val message: String
)