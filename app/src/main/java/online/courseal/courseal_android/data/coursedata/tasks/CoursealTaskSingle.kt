package online.courseal.courseal_android.data.coursedata.tasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@Serializable
@SerialName("test_single_answer")
data class CoursealTaskSingle(
    val body: EditorJSContent,
    val options: List<String>,
    @SerialName("correct_option") val correctOption: Int
) : CoursealTask()