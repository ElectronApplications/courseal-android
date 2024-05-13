package online.courseal.courseal_android.ui.viewmodels.course

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
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskMultipleExamAnswer
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskPracticeTrainingAnswer
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskSingleExamAnswer
import online.courseal.courseal_android.data.coursedata.enrolltaskscomplete.TaskTypeExamAnswer
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTask
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskMultiple
import online.courseal.courseal_android.data.coursedata.examtasks.CoursealExamTaskSingle
import online.courseal.courseal_android.data.coursedata.examtasks.ExamTaskMultipleOption
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskMultiple
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskSingle
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject
import kotlin.properties.Delegates

enum class LessonStartUiError {
    UNKNOWN,
    NONE
}

sealed class LessonStartUiContent {
    class LectureContent(val content: EditorJSContent) : LessonStartUiContent()
    class TaskContent(val content: CoursealExamTask) : LessonStartUiContent()
    class ResultsContent(val xp: Int, val score: Float?) : LessonStartUiContent()
}

data class LessonStartUiState(
    val loading: Boolean = true,
    val currentContent: LessonStartUiContent? = null,
    val confirmEnabled: Boolean = false,
    val selectedOptions: List<Int> = emptyList(),
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

    private var currentTask: Int? = null
    private var selectedOptions: List<Int> = emptyList()

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

                updateCurrent()
            }
        }
    }

    private suspend fun updateCurrent() {
        currentTask = tasksStack.removeFirstOrNull()
        if (currentTask != null) {
            selectedOptions = emptyList()
            _uiState.update {
                it.copy(
                    currentContent = when (val tasks = tasks!!.tasks) {
                        is EnrollTasksLecture -> LessonStartUiContent.LectureContent(
                            content = tasks.lectureContent
                        )
                        is EnrollTasksExam -> LessonStartUiContent.TaskContent(
                            content = tasks.tasks[currentTask!!].task
                        )
                        is EnrollTasksPracticeTraining -> LessonStartUiContent.TaskContent(
                            content = when (val practiceTask = tasks.tasks[currentTask!!].task) {
                                is CoursealTaskSingle -> CoursealExamTaskSingle(
                                    body = practiceTask.body,
                                    options = practiceTask.options
                                )
                                is CoursealTaskMultiple -> CoursealExamTaskMultiple(
                                    body = practiceTask.body,
                                    options = practiceTask.options.map { option -> ExamTaskMultipleOption(option.text) }
                                )
                            }
                        )
                    },
                    selectedOptions = selectedOptions
                )
            }

            _uiState.update {
                it.copy(
                    confirmEnabled = when (val content = _uiState.value.currentContent) {
                        is LessonStartUiContent.LectureContent -> true
                        is LessonStartUiContent.ResultsContent -> true
                        is LessonStartUiContent.TaskContent -> content.content is CoursealExamTaskMultiple
                        null -> false
                    }
                )
            }
        } else {
            finishLesson()
        }
    }

    suspend fun confirmAnswer() {
        when (_uiState.value.currentContent) {
            is LessonStartUiContent.LectureContent -> updateCurrent()
            is LessonStartUiContent.TaskContent -> {
                when (val currentAnswers = answers) {
                    is EnrollTasksCompleteLecture -> {}
                    is EnrollTasksCompletePracticeTraining -> {
                        val task = (tasks!!.tasks as EnrollTasksPracticeTraining).tasks[currentTask!!].task
                        val correct = when (task) {
                            is CoursealTaskSingle -> selectedOptions.getOrNull(0) == task.correctOption
                            is CoursealTaskMultiple -> {
                                val correctOptions = task.options
                                    .mapIndexed { index, option -> Pair(index, option.isCorrect) }
                                    .filter { it.second }
                                    .map { it.first }
                                selectedOptions.toSet() == correctOptions.toSet()
                            }
                        }

                        answers = currentAnswers.copy(tasks = currentAnswers.tasks.toMutableList().apply { add(
                            TaskPracticeTrainingAnswer(currentTask!!, correct)
                        ) })
                    }
                    is EnrollTasksCompleteExam -> {
                        val task = (tasks!!.tasks as EnrollTasksPracticeTraining).tasks[currentTask!!].task
                        val answer: TaskTypeExamAnswer = when (task) {
                            is CoursealTaskSingle -> TaskSingleExamAnswer(selectedOptions.getOrElse(0) { 0 })
                            is CoursealTaskMultiple -> TaskMultipleExamAnswer(selectedOptions)
                        }
                        answers = currentAnswers.copy(tasks = currentAnswers.tasks.toMutableList().apply { add(
                            TaskExamAnswer(currentTask!!, answer)
                        ) })
                    }
                }
                updateCurrent()
            }
            is LessonStartUiContent.ResultsContent -> TODO()
            null -> {}
        }
    }

    fun selectOption(index: Int) {
        val isSingle = when (val tasks = tasks!!.tasks) {
            is EnrollTasksLecture -> true
            is EnrollTasksPracticeTraining -> tasks.tasks[currentTask!!].task is CoursealTaskSingle
            is EnrollTasksExam -> tasks.tasks[currentTask!!].task is CoursealExamTaskSingle
        }

        selectedOptions = if (isSingle) {
            if (index in selectedOptions) emptyList() else listOf(index)
        } else {
            if (index in selectedOptions)
                selectedOptions.toMutableList().apply { remove(index) }
            else
                selectedOptions.toMutableList().apply { add(index) }
        }
        _uiState.update {
            it.copy(
                selectedOptions = selectedOptions,
                confirmEnabled = !isSingle || selectedOptions.isNotEmpty()
            )
        }
    }

    private suspend fun finishLesson() {
        courseEnrollmentService.completeTasks(courseId, lessonId, tasks!!.lessonToken, answers)
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = LessonStartUiError.NONE) }
    }

}