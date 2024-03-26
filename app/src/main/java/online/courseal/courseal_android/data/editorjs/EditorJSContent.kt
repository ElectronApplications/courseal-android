package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.Serializable

@Serializable
class EditorJSContent(
    val time: Long,
    val blocks: List<EditorJSBlock>
)