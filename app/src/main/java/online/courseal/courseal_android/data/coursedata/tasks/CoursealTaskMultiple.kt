package online.courseal.courseal_android.data.coursedata.tasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@Serializable
@SerialName("test_multiple_answers")
data class CoursealTaskMultiple(
    val body: EditorJSContent,
    val options: List<TaskMultipleOption>,
) : CoursealTask()

@Serializable
data class TaskMultipleOption(
    val text: String,
    @SerialName("is_correct") val isCorrect: Boolean
)