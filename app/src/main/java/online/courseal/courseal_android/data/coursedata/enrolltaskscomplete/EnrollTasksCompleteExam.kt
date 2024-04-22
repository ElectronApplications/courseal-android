package online.courseal.courseal_android.data.coursedata.enrolltaskscomplete

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("exam")
data class EnrollTasksCompleteExam(
    val tasks: List<TaskExamAnswer>
) : EnrollTasksComplete()

@Serializable
data class TaskExamAnswer(
    @SerialName("task_id") val taskId: Int,
    val answer: TaskTypeExamAnswer
)

@Serializable
sealed class TaskTypeExamAnswer

@Serializable
@SerialName("test_single_answer")
data class TaskSingleExamAnswer(
    val option: Int
) : TaskTypeExamAnswer()

@Serializable
@SerialName("test_multiple_answers")
data class TaskMultipleExamAnswer(
    val options: List<Int>
) : TaskTypeExamAnswer()