package online.courseal.courseal_android.data.coursedata.lessonenroll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTask

@Serializable
@SerialName("exam")
data class CoursealLessonEnrollExam(
    val tasks: List<CoursealLessonEnrollExamTask>
) : CoursealLessonEnroll()

@Serializable
data class CoursealLessonEnrollExamTask(
    @SerialName("task_id") val taskId: Int,
    val task: CoursealExamTask
)