package online.courseal.courseal_android.data.api.courseenrollment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.enrolltasks.EnrollTasks

@Serializable
data class TasksApiResponse(
    @SerialName("lesson_token") val lessonToken: String,
    val tasks: EnrollTasks
)

enum class TasksApiError {
    COURSE_NOT_FOUND,
    LESSON_NOT_FOUND,
    UNKNOWN
}