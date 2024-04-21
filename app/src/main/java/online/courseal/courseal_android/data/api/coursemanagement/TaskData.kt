package online.courseal.courseal_android.data.api.coursemanagement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask

@Serializable
data class TaskCreateApiRequest(
    @SerialName("task_name") val taskName: String,
    val task: CoursealTask
)

@Serializable
data class TaskCreateApiResponse(
    @SerialName("task_id") val taskId: Int
)