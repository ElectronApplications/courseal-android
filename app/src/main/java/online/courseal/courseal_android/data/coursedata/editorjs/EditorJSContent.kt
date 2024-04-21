package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.Serializable

@Serializable
data class EditorJSContent(
    val time: Long?,
    val blocks: List<EditorJSBlock>,
    val version: String?
)