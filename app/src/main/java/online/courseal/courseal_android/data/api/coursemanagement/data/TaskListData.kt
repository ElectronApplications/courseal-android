package online.courseal.courseal_android.data.api.coursemanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask

@Serializable
data class TaskListData(
    @SerialName("task_id") val taskId: Int,
    @SerialName("task_name") val taskName: String,
    val task: CoursealTask
)

enum class TaskListApiError {
    NO_PERMISSIONS,
    UNKNOWN
}