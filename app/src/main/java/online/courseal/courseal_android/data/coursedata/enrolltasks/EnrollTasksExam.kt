package online.courseal.courseal_android.data.coursedata.enrolltasks

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTask

@Serializable
@SerialName("exam")
data class EnrollTasksExam(
    val tasks: List<CoursealLessonEnrollExamTask>
) : EnrollTasks()

@Serializable
data class CoursealLessonEnrollExamTask(
    @SerialName("task_id") val taskId: Int,
    val task: CoursealExamTask
)