package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("math")
data class EditorJSLatex (
    override val id: String,
    val data: EditorJSLatexData
) : EditorJSBlock()

@Serializable
data class EditorJSLatexData(
    val math: String
)