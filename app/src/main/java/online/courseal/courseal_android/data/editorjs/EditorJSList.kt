package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("list")
class EditorJSList (
    override val id: String,
    val data: EditorJSListData
) : EditorJSBlock()

@Serializable
data class EditorJSListData(
    val style: EditorJSListStyle,
    val items: List<String>
)

enum class EditorJSListStyle(val style: String) {
    ORDERED("ordered"),
    UNORDERED("unordered")
}