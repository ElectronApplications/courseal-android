package online.courseal.courseal_android.data.coursedata.lessons

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@Serializable
@SerialName("lecture")
data class CoursealLessonLecture(
    @SerialName("lecture_content") val lectureContent: EditorJSContent
) : CoursealLesson()