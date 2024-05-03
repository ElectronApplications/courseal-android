package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLesson

@Serializable
data class UpdateLessonApiRequest(
    @SerialName("lesson_name") val lessonName: String,
    @SerialName("lesson_progress_needed") val lessonProgressNeeded: Int,
    val lesson: CoursealLesson
)

enum class UpdateLessonApiError {
    NO_PERMISSIONS,
    UNKNOWN
}