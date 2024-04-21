package online.courseal.courseal_android.data.coursedata.lessonenroll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@Serializable
@SerialName("lecture")
data class CoursealLessonEnrollLecture(
    @SerialName("lecture_content") val lectureContent: EditorJSContent
) : CoursealLessonEnroll()