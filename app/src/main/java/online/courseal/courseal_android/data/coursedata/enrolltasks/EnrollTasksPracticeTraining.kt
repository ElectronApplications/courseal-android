package online.courseal.courseal_android.data.coursedata.enrolltasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask

@Serializable
@SerialName("practice_training")
data class EnrollTasksPracticeTraining(
    val tasks: List<CoursealLessonEnrollPracticeTask>
) : EnrollTasks()

@Serializable
data class CoursealLessonEnrollPracticeTask(
    @SerialName("task_id") val taskId: Int,
    val task: CoursealTask
)