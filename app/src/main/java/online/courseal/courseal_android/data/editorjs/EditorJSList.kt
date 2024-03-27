package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("list")
data class EditorJSList (
    override val id: String,
    val data: EditorJSListData
) : EditorJSBlock()

@Serializable
data class EditorJSListData(
    val style: EditorJSListStyle,
    val items: List<String>
)

@Serializable
enum class EditorJSListStyle {
    @SerialName("ordered") ORDERED,
    @SerialName("unordered") UNORDERED
}