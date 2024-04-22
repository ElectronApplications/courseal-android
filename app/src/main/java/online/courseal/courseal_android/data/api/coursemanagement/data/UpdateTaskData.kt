package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask

@Serializable
data class UpdateTaskApiRequest(
    @SerialName("task_name") val taskName: String,
    val task: CoursealTask
)

enum class UpdateTaskApiError {
    NO_PERMISSIONS,
    UNKNOWN
}