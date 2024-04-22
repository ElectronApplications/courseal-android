package online.courseal.courseal_android.data.coursedata.editorjs

import kotlinx.serialization.Serializable

@Serializable
sealed class EditorJSBlock {
    abstract val id: String
}