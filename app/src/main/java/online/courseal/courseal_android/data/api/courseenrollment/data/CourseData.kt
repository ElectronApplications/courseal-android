package online.courseal.courseal_android.data.api.courseenrollment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CourseEnrollInfoApiResponse(
    @SerialName("course_id") val courseId: Int,
    @SerialName("course_name") val courseName: String,
    @SerialName("course_description") val courseDescription: String,
    val rating: Int,
    val xp: Int,
    val lessons: List<List<CourseEnrollLessonData>>
)

@Serializable
data class CourseEnrollLessonData(
    @SerialName("lesson_id") val lessonId: Int,
    @SerialName("lesson_name") val lessonName: String,
    @SerialName("lesson_progress") val lessonProgress: Int,
    @SerialName("lesson_progress_needed") val lessonProgressNeeded: Int,
    @SerialName("can_be_done") val canBeDone: Boolean
)

enum class CourseEnrollInfoApiError {
    COURSE_NOT_FOUND,
    UNKNOWN
}