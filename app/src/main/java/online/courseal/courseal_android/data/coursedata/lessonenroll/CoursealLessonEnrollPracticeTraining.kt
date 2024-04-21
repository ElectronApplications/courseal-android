package online.courseal.courseal_android.data.coursedata.lessonenroll

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask

@Serializable
@SerialName("practice_training")
data class CoursealLessonEnrollPracticeTraining(
    val tasks: List<CoursealLessonEnrollPracticeTask>
) : CoursealLessonEnroll()

@Serializable
data class CoursealLessonEnrollPracticeTask(
    @SerialName("task_id") val taskId: Int,
    val task: CoursealTask
)