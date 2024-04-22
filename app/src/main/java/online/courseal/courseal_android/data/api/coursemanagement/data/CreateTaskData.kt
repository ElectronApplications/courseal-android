package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask

@Serializable
data class CreateTaskApiRequest(
    @SerialName("task_name") val taskName: String,
    val task: CoursealTask
)

@Serializable
data class CreateTaskApiResponse(
    @SerialName("task_id") val taskId: Int
)

enum class CreateTaskApiError {
    NO_PERMISSIONS,
    UNKNOWN
}