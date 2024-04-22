package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("image")
data class EditorJSImage (
    override val id: String,
    val data: EditorJSImageData
) : EditorJSBlock()

@Serializable
data class EditorJSImageData(
    val file: EditorJsImageFile,
    val caption: String,
    val withBorder: Boolean,
    val withBackground: Boolean,
    val stretched: Boolean
)

@Serializable
data class EditorJsImageFile(
    val url: String
)