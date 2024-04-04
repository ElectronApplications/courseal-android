package online.courseal.courseal_android.data.editorjs

import kotlinx.serialization.Serializable

@Serializable
sealed class EditorJSBlock {
    abstract val id: String
}