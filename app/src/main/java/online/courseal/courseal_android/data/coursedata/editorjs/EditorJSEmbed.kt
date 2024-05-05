package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("embed")
data class EditorJSEmbed(
    override val id: String,
    val data: EditorJSEmbedData
) : EditorJSBlock()

@Serializable
data class EditorJSEmbedData(
    val service: EditorJSEmbedService,
    val source: String,
    val embed: String,
    val width: Int,
    val height: Int,
    val caption: String
)

@Serializable
enum class EditorJSEmbedService {
    @SerialName("youtube") YOUTUBE
}