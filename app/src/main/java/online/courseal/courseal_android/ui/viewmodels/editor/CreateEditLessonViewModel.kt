package online.courseal.courseal_android.ui.viewmodels.editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.R
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.coursemanagement.CoursealCourseManagementService
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeader
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeaderData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeaderLevel
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLesson
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonExam
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonLecture
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonPractice
import online.courseal.courseal_android.data.coursedata.lessons.CoursealLessonTraining
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

val defaultLesson: CoursealLesson = CoursealLessonLecture(
    lectureContent = EditorJSContent(
        time = null,
        blocks = listOf(
            EditorJSHeader(
                id = "abcdef",
                data = EditorJSHeaderData(
                    "Header",
                    level = EditorJSHeaderLevel.H1
                )
            )
        ),
        version = null
    )
)

enum class CoursealLessonType(val nameId: Int) {
    LECTURE(R.string.lecture),
    PRACTICE(R.string.practice),
    TRAINING(R.string.training),
    EXAM(R.string.exam)
}

enum class CreateEditLessonUiError {
    UNKNOWN,
    NONE
}

data class CreateEditLessonUiState(
    val makingRequest: Boolean = true,
    val isCreating: Boolean = true,
    val lessonName: String = "",
    val progressNeeded: Int = 1,
    val lesson: CoursealLesson = defaultLesson,
    val tasks: List<String>? = null,
    val availableTasks: List<Pair<String, Int>>? = null,
    val saveEndpoint: String = "",
    val errorState: CreateEditLessonUiError = CreateEditLessonUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class CreateEditLessonViewModel @Inject constructor(
    state: SavedStateHandle,
    private val courseManagementService: CoursealCourseManagementService,
    private val serverDao: ServerDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEditLessonUiState())
    val uiState: StateFlow<CreateEditLessonUiState> = _uiState.asStateFlow()

    private var isCreating by Delegates.notNull<Boolean>()
    private var lessonId: Int? = null

    private var lessonName = ""
    private var progressNeeded = 1
    private var lesson = defaultLesson
    private lateinit var editorViewModel: EditorViewModel

    init {
        isCreating = state["isCreating"]!!

        if (!isCreating) {
            lessonId = state["lessonId"]!!
        }

        viewModelScope.launch {
            _uiState.update { it.copy(saveEndpoint = "${serverDao.getCurrentServerUrl()!!}/api/static/editorjsimage") }
        }
    }

    fun passEditorViewModel(editorViewModel: EditorViewModel) {
        this.editorViewModel = editorViewModel

        if (!isCreating) {
            val editorLesson = editorViewModel.uiState.value.courseLessons!!.find { it.lessonId == lessonId }!!
            lessonName = editorLesson.lessonName
            progressNeeded = editorLesson.lessonProgressNeeded
            lesson = editorLesson.lesson
        }

        updateLesson(lesson)
        _uiState.update {
            it.copy(
                isCreating = isCreating,
                lessonName = lessonName,
                progressNeeded = progressNeeded,
                makingRequest = false
            )
        }
    }

    fun updateLessonName(lessonName: String) {
        this.lessonName = lessonName
        _uiState.update { it.copy(lessonName = lessonName) }
    }

    fun updateProgressNeeded(progressNeeded: Int) {
        this.progressNeeded = when(val currentLesson = lesson) {
            is CoursealLessonLecture -> 1
            is CoursealLessonPractice -> min(progressNeeded, max(1, currentLesson.tasks.size / 4))
            is CoursealLessonExam -> min(progressNeeded, max(1, currentLesson.tasks.size / 4))
            is CoursealLessonTraining -> progressNeeded
        }
        _uiState.update { it.copy(progressNeeded = this.progressNeeded) }
    }

    private fun updateLesson(lesson: CoursealLesson) {
        this.lesson = lesson
        _uiState.update {
            it.copy(
                lesson = lesson,
                tasks = when (lesson) {
                    is CoursealLessonLecture -> null
                    is CoursealLessonTraining -> null
                    is CoursealLessonExam -> lesson.tasks.map { taskId ->
                        editorViewModel.uiState.value.courseTasks!!.find {
                            courseTask -> courseTask.taskId == taskId
                        }!!.taskName
                    }
                    is CoursealLessonPractice -> lesson.tasks.map { taskId ->
                        editorViewModel.uiState.value.courseTasks!!.find {
                            courseTask -> courseTask.taskId == taskId
                        }!!.taskName
                    }
                },
                availableTasks = when (lesson) {
                    is CoursealLessonLecture -> null
                    is CoursealLessonTraining -> null
                    is CoursealLessonExam -> editorViewModel.uiState.value.courseTasks!!.filter { courseTask ->
                        !lesson.tasks.contains(courseTask.taskId)
                    }.map { courseTask ->
                        Pair(courseTask.taskName, courseTask.taskId)
                    }
                    is CoursealLessonPractice -> editorViewModel.uiState.value.courseTasks!!.filter { courseTask ->
                        !lesson.tasks.contains(courseTask.taskId)
                    }.map { courseTask ->
                        Pair(courseTask.taskName, courseTask.taskId)
                    }
                },
            )
        }
    }

    fun updateLessonType(type: CoursealLessonType) {
        updateLesson(when (type) {
            CoursealLessonType.LECTURE -> {
                progressNeeded = 1
                if (lesson is CoursealLessonLecture) lesson else defaultLesson
            }
            CoursealLessonType.PRACTICE -> when (val currentLesson = lesson) {
                is CoursealLessonLecture -> CoursealLessonPractice(emptyList())
                is CoursealLessonPractice -> currentLesson
                is CoursealLessonTraining -> CoursealLessonPractice(emptyList())
                is CoursealLessonExam -> CoursealLessonPractice(currentLesson.tasks)
            }
            CoursealLessonType.TRAINING -> CoursealLessonTraining
            CoursealLessonType.EXAM -> when (val currentLesson = lesson) {
                is CoursealLessonLecture -> CoursealLessonExam(emptyList())
                is CoursealLessonPractice -> CoursealLessonExam(currentLesson.tasks)
                is CoursealLessonTraining -> CoursealLessonExam(emptyList())
                is CoursealLessonExam -> currentLesson
            }
        })
    }

    fun updateLectureContent(lectureContent: EditorJSContent) {
        val currentLesson = lesson
        if (currentLesson is CoursealLessonLecture) {
            updateLesson(currentLesson.copy(lectureContent = lectureContent))
        }
    }

    fun removeTask(index: Int) {
        updateLesson(when (val currentLesson = lesson) {
            is CoursealLessonLecture -> currentLesson
            is CoursealLessonTraining -> currentLesson
            is CoursealLessonPractice -> currentLesson.copy(
                tasks = currentLesson.tasks.toMutableList().apply { removeAt(index) }
            )
            is CoursealLessonExam -> currentLesson.copy(
                tasks = currentLesson.tasks.toMutableList().apply { removeAt(index) }
            )
        })
    }

    fun addTask(taskId: Int) {
        updateLesson(when (val currentLesson = lesson) {
            is CoursealLessonLecture -> currentLesson
            is CoursealLessonTraining -> currentLesson
            is CoursealLessonPractice -> currentLesson.copy(
                tasks = currentLesson.tasks.toMutableList().apply { add(taskId) }
            )
            is CoursealLessonExam -> currentLesson.copy(
                tasks = currentLesson.tasks.toMutableList().apply { add(taskId) }
            )
        })
    }

    suspend fun confirm(onGoBack: () -> Unit) {
        _uiState.update { it.copy(makingRequest = true)  }

        if (isCreating) {
            when (val createResult = courseManagementService
                    .createLesson(editorViewModel.uiState.value.currentCourseId!!, lessonName, progressNeeded, lesson)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = createResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditLessonUiError.UNKNOWN) }
                is ApiResult.Success -> {
                    editorViewModel.setNeedUpdate()
                    onGoBack()
                }
            }
        } else {
            when (val updateResult = courseManagementService
                    .updateLesson(editorViewModel.uiState.value.currentCourseId!!, lessonId!!, lessonName, progressNeeded, lesson)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = updateResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditLessonUiError.UNKNOWN) }
                is ApiResult.Success -> {
                    editorViewModel.setNeedUpdate()
                    onGoBack()
                }
            }
        }

        _uiState.update { it.copy(makingRequest = false) }
    }

    suspend fun delete(onGoBack: () -> Unit) {
        _uiState.update { it.copy(makingRequest = true)  }

        when (val deleteResult = courseManagementService
                .deleteLesson(editorViewModel.uiState.value.currentCourseId!!, lessonId!!)) {
            is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = deleteResult.unrecoverableType) }
            is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditLessonUiError.UNKNOWN) }
            is ApiResult.Success -> {
                editorViewModel.setNeedUpdate()
                onGoBack()
            }
        }

        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = CreateEditLessonUiError.NONE) }
    }
}