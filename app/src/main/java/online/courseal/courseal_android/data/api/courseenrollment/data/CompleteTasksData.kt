package online.courseal.courseal_android.data.api.courseenrollment.data

import kotlinx.datetime.TimeZone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.EnrollTasksComplete

@Serializable
data class CompleteTasksApiRequest(
    @SerialName("lesson_token") val lessonToken: String,
    val timezone: TimeZone,
    val results: EnrollTasksComplete
)

@Serializable
data class CompleteTasksApiResponse(
    val xp: Int,
    val completed: Boolean,
    val mistakes: List<CompleteTaskMistake>?
)

enum class CompleteTasksApiError {
    TOKEN_INVALID,
    COURSE_NOT_FOUND,
    LESSON_NOT_FOUND,
    BAD_REQUEST,
    UNKNOWN,
}

@Serializable
data class CompleteTaskMistake(
    @SerialName("task_id") val taskId: Int,
    val correct: Boolean
)
