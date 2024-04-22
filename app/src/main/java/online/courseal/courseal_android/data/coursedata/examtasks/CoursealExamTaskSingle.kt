package online.courseal.courseal_android.data.coursedata.examtasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@Serializable
@SerialName("test_single_answer")
data class CoursealExamTaskSingle(
    val body: EditorJSContent,
    val options: List<String>
) : CoursealExamTask()