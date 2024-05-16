package online.courseal.courseal_android.ui.viewmodels.course

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.courseenrollment.CoursealCourseEnrollmentService
import online.courseal.courseal_android.data.api.courseenrollment.data.TasksApiResponse
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent
import online.courseal.courseal_android.data.coursedata.enrolltasks.EnrollTasksExam
import online.courseal.courseal_android.data.coursedata.enrolltasks.EnrollTasksLecture
import online.courseal.courseal_android.data.coursedata.enrolltasks.EnrollTasksPracticeTraining
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.EnrollTasksComplete
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.EnrollTasksCompleteExam
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.EnrollTasksCompleteLecture
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.EnrollTasksCompletePracticeTraining
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskExamAnswer
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskPracticeTrainingAnswer
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskTypeExamAnswer
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTask
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject
import kotlin.properties.Delegates

enum class LessonStartUiError {
    UNKNOWN,
    NONE
}

sealed class LessonTask {
    class PracticeTrainingTask(val content: CoursealTask) : LessonTask()
    class ExamTask(val content: CoursealExamTask) : LessonTask()
}

sealed class TaskAnswer {
    class PracticeTrainingAnswer(val isCorrect: Boolean) : TaskAnswer()
    class ExamAnswer(val answer: TaskTypeExamAnswer) : TaskAnswer()
}

sealed class LessonStartUiContent {
    class LectureContent(val content: EditorJSContent) : LessonStartUiContent()
    class TaskContent(val task: LessonTask) : LessonStartUiContent()
    class ResultsContent(val xp: Int, val completed: Boolean) : LessonStartUiContent()
}

data class LessonStartUiState(
    val loading: Boolean = true,
    val currentContent: LessonStartUiContent? = null,
    val taskCorrect: Boolean? = null,
    val tasksProgress: Float? = null,
    val errorState: LessonStartUiError = LessonStartUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class LessonStartViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val courseEnrollmentService: CoursealCourseEnrollmentService
) : ViewModel() {
    private val _uiState = MutableStateFlow(LessonStartUiState())
    val uiState: StateFlow<LessonStartUiState> = _uiState.asStateFlow()

    private var courseId by Delegates.notNull<Int>()
    private var lessonId by Delegates.notNull<Int>()
    private var tasks: TasksApiResponse? = null
    private var tasksAmount: Int? = null

    private var currentTask: Int? = null

    private lateinit var tasksStack: ArrayDeque<Int>
    private lateinit var answers: EnrollTasksComplete

    init {
        viewModelScope.launch {
            courseId = userDao.getCurrentUser()!!.currentCourseId!!
            lessonId = state["lessonId"]!!

            tasks = when(val tasksResult = courseEnrollmentService.getTasks(courseId, lessonId)) {
                is ApiResult.Error -> {
                    _uiState.update { it.copy(errorState = LessonStartUiError.UNKNOWN) }
                    null
                }
                is ApiResult.UnrecoverableError -> {
                    _uiState.update { it.copy(errorUnrecoverableState = tasksResult.unrecoverableType) }
                    null
                }
                is ApiResult.Success -> tasksResult.successValue
            }

            _uiState.update { it.copy(loading = false) }

            tasks?.let { tasks ->
                tasksStack = when (tasks.tasks) {
                    is EnrollTasksLecture -> ArrayDeque(listOf(0))
                    is EnrollTasksExam -> ArrayDeque((0..<tasks.tasks.tasks.size).toList())
                    is EnrollTasksPracticeTraining -> ArrayDeque((0..<tasks.tasks.tasks.size).toList())
                }

                answers = when (tasks.tasks) {
                    is EnrollTasksLecture -> EnrollTasksCompleteLecture
                    is EnrollTasksPracticeTraining -> EnrollTasksCompletePracticeTraining(emptyList())
                    is EnrollTasksExam -> EnrollTasksCompleteExam(emptyList())
                }

                tasksAmount = when (tasks.tasks) {
                    is EnrollTasksLecture -> null
                    is EnrollTasksExam -> tasks.tasks.tasks.size
                    is EnrollTasksPracticeTraining -> tasks.tasks.tasks.size
                }

                getNextTask(false)
            }
        }
    }

    private fun getNextTask(postponeCurrent: Boolean) {
        if (postponeCurrent)
            tasksStack.addLast(currentTask!!)

        currentTask = tasksStack.removeFirstOrNull()
        if (currentTask != null) {
            _uiState.update {
                it.copy(
                    currentContent = when (val tasks = tasks!!.tasks) {
                        is EnrollTasksLecture -> LessonStartUiContent.LectureContent(tasks.lectureContent)
                        is EnrollTasksExam -> LessonStartUiContent.TaskContent(LessonTask.ExamTask(tasks.tasks[currentTask!!].task))
                        is EnrollTasksPracticeTraining -> LessonStartUiContent.TaskContent(LessonTask.PracticeTrainingTask(tasks.tasks[currentTask!!].task))
                    },
                    tasksProgress = tasksAmount?.let { amount -> (amount - tasksStack.size - 1).toFloat() / amount  }
                )
            }
        } else {
            finishLesson()
        }
    }

    fun saveAnswer(answer: TaskAnswer) {
         when (answer) {
            is TaskAnswer.PracticeTrainingAnswer -> {
                val currentAnswers = answers as EnrollTasksCompletePracticeTraining
                val taskId = (tasks!!.tasks as EnrollTasksPracticeTraining).tasks[currentTask!!].taskId
                if (!currentAnswers.tasks.any { it.taskId == taskId }) {
                    answers = currentAnswers.copy(
                        tasks = currentAnswers.tasks.toMutableList().apply { add(
                            TaskPracticeTrainingAnswer(taskId, answer.isCorrect)
                        ) }
                    )
                }

                _uiState.update { it.copy(taskCorrect = answer.isCorrect) }
            }
            is TaskAnswer.ExamAnswer -> {
                val currentAnswers = answers as EnrollTasksCompleteExam
                val taskId = (tasks!!.tasks as EnrollTasksExam).tasks[currentTask!!].taskId
                answers = currentAnswers.copy(
                    tasks = currentAnswers.tasks.toMutableList().apply {
                        add(TaskExamAnswer(taskId, answer.answer))
                    }
                )
                getNextTask(false)
            }
        }
    }

    fun finishLesson() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            when (val finishResult = courseEnrollmentService.completeTasks(courseId, lessonId, tasks!!.lessonToken, answers)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = finishResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = LessonStartUiError.UNKNOWN) }
                is ApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            currentContent = LessonStartUiContent.ResultsContent(
                                xp = finishResult.successValue.xp,
                                completed = finishResult.successValue.completed
                            ),
                            loading = false
                        )
                    }
                }
            }
        }
    }

    fun hideTaskCorrect() {
        val correct = _uiState.value.taskCorrect
        _uiState.update { it.copy(taskCorrect = null) }
        getNextTask(correct == false)
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = LessonStartUiError.NONE) }
    }

}