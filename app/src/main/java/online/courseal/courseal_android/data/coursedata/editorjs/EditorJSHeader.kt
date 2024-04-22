package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("header")
data class EditorJSHeader (
    override val id: String,
    val data: EditorJSHeaderData
) : EditorJSBlock()

@Serializable
data class EditorJSHeaderData(
    val text: String,
    val level: EditorJSHeaderLevel
)

@Serializable
enum class EditorJSHeaderLevel {
    @SerialName("1") H1,
    @SerialName("2") H2,
    @SerialName("3") H3,
    @SerialName("4") H4,
    @SerialName("5") H5,
    @SerialName("6") H6
}