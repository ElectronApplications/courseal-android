package online.courseal.courseal_android.data.coursedata.examtasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@Serializable
@SerialName("test_multiple_answers")
data class CoursealExamTaskMultiple(
    val body: EditorJSContent,
    val options: List<ExamTaskMultipleOption>
) : CoursealExamTask()

@Serializable
data class ExamTaskMultipleOption(
    val text: String,
)