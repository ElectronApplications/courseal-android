package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLesson

@Serializable
data class CreateLessonApiRequest(
    @SerialName("lesson_name") val lessonName: String,
    @SerialName("lesson_progress_needed") val lessonProgressNeeded: Int,
    val lesson: CoursealLesson
)

@Serializable
data class CreateLessonApiResponse(
    @SerialName("lesson_id") val lessonId: Int
)

enum class CreateLessonApiError {
    NO_PERMISSIONS,
    UNKNOWN
}