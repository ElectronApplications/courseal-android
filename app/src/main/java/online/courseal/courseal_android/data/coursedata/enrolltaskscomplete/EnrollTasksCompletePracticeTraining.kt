package online.courseal.courseal_android.data.coursedata.enrolltaskscomplete

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("practice_training")
data class EnrollTasksCompletePracticeTraining(
    val tasks: List<TaskPracticeTrainingAnswer>
) : EnrollTasksComplete()

@Serializable
data class TaskPracticeTrainingAnswer(
    @SerialName("task_id") val taskId: Int,
    val correct: Boolean
)