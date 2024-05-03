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
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTask
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskMultiple
import online.courseal.courseal_android.data.coursedata.tasks.CoursealTaskSingle
import online.courseal.courseal_android.data.coursedata.tasks.TaskMultipleOption
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject
import kotlin.properties.Delegates

val defaultTask: CoursealTask = CoursealTaskSingle(
    body = EditorJSContent(
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
    ),
    options = listOf("Option 1"),
    correctOption = 0
)

enum class CoursealTaskType(val nameId: Int) {
    SINGLE(R.string.single_option),
    MULTIPLE(R.string.multiple_option)
}

enum class CreateEditTaskUiError {
    UNKNOWN,
    NONE
}

data class CreateEditTaskUiState(
    val makingRequest: Boolean = true,
    val isCreating: Boolean = true,
    val taskName: String = "",
    val task: CoursealTask = defaultTask,
    val options: List<Pair<String, Boolean>> = listOf(Pair("Option 1", true)),
    val saveEndpoint: String = "",
    val errorState: CreateEditTaskUiError = CreateEditTaskUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class CreateEditTaskViewModel @Inject constructor(
    state: SavedStateHandle,
    private val courseManagementService: CoursealCourseManagementService,
    private val serverDao: ServerDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEditTaskUiState())
    val uiState: StateFlow<CreateEditTaskUiState> = _uiState.asStateFlow()

    private var isCreating by Delegates.notNull<Boolean>()
    private var taskId: Int? = null

    private var taskName = ""
    private var task = defaultTask
    private lateinit var editorViewModel: EditorViewModel

    init {
        isCreating = state["isCreating"]!!

        if (!isCreating) {
            taskId = state["taskId"]!!
        }

        viewModelScope.launch {
            _uiState.update { it.copy(saveEndpoint = "${serverDao.getCurrentServerUrl()!!}/api/static/editorjsimage") }
        }
    }

    fun passEditorViewModel(editorViewModel: EditorViewModel) {
        this.editorViewModel = editorViewModel

        if (!isCreating) {
            val editorTask = editorViewModel.uiState.value.courseTasks!!.find { it.taskId == taskId }!!
            taskName = editorTask.taskName
            task = editorTask.task
        }

        updateTask(task)
        _uiState.update {
            it.copy(
                isCreating = isCreating,
                taskName = taskName,
                makingRequest = false
            )
        }
    }

    fun updateTaskName(taskName: String) {
        this.taskName = taskName
        _uiState.update { it.copy(taskName = taskName) }
    }

    private fun updateTask(task: CoursealTask) {
        this.task = task
        _uiState.update {
            it.copy(
                task = task,
                options = when (task) {
                    is CoursealTaskMultiple -> task.options.map { option -> Pair(option.text, option.isCorrect) }
                    is CoursealTaskSingle -> task.options.mapIndexed { index, option -> Pair(option, index == task.correctOption) }
                }
            )
        }
    }

    fun updateTaskType(type: CoursealTaskType) {
        updateTask(when (type) {
            CoursealTaskType.SINGLE -> when (val currentTask = task) {
                is CoursealTaskMultiple -> CoursealTaskSingle(
                    body = currentTask.body,
                    options = currentTask.options.map { it.text },
                    correctOption = 0
                )
                is CoursealTaskSingle -> currentTask
            }
            CoursealTaskType.MULTIPLE -> when (val currentTask = task) {
                is CoursealTaskMultiple -> currentTask
                is CoursealTaskSingle -> CoursealTaskMultiple(
                    body = currentTask.body,
                    options = currentTask.options.mapIndexed { index, option ->
                        TaskMultipleOption(option, index == currentTask.correctOption)
                    }
                )
            }
        })
    }

    fun updateTaskBody(body: EditorJSContent) {
        updateTask(when (val currentTask = task) {
            is CoursealTaskMultiple -> currentTask.copy(body = body)
            is CoursealTaskSingle -> currentTask.copy(body = body)
        })
    }

    fun addOption() {
        updateTask(when (val currentTask = task) {
            is CoursealTaskMultiple -> currentTask.copy(
                options = currentTask.options.toMutableList().apply {
                    add(TaskMultipleOption("", false))
                }
            )
            is CoursealTaskSingle -> currentTask.copy(
                options = currentTask.options.toMutableList().apply { add("") }
            )
        })
    }

    fun removeOption(index: Int) {
        if (_uiState.value.options.size > 1) {
            updateTask(when (val currentTask = task) {
                is CoursealTaskMultiple -> currentTask.copy(
                    options = currentTask.options.toMutableList().apply { removeAt(index) }
                )
                is CoursealTaskSingle -> currentTask.copy(
                    options = currentTask.options.toMutableList().apply { removeAt(index) }
                )
            })
        }
    }

    fun checkOption(index: Int) {
        updateTask(when (val currentTask = task) {
            is CoursealTaskMultiple -> currentTask.copy(
                options = currentTask.options.toMutableList().apply { this[index] = this[index].copy(isCorrect = !this[index].isCorrect) }
            )
            is CoursealTaskSingle -> currentTask.copy(correctOption = index)
        })
    }

    fun updateOptionText(index: Int, optionText: String) {
        updateTask(when (val currentTask = task) {
            is CoursealTaskMultiple -> currentTask.copy(
                options = currentTask.options.toMutableList().apply { this[index] = this[index].copy(text = optionText) }
            )
            is CoursealTaskSingle -> currentTask.copy(
                options = currentTask.options.toMutableList().apply { this[index] = optionText }
            )
        })
    }

    suspend fun confirm(onGoBack: () -> Unit) {
        _uiState.update { it.copy(makingRequest = true)  }

        if (isCreating) {
            when (val createResult = courseManagementService
                    .createTask(editorViewModel.uiState.value.currentCourseId!!, taskName, task)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = createResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditTaskUiError.UNKNOWN) }
                is ApiResult.Success -> {
                    editorViewModel.setNeedUpdate()
                    onGoBack()
                }
            }
        } else {
            when (val updateResult = courseManagementService
                    .updateTask(editorViewModel.uiState.value.currentCourseId!!, taskId!!, taskName, task)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = updateResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditTaskUiError.UNKNOWN) }
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
                .deleteTask(editorViewModel.uiState.value.currentCourseId!!, taskId!!)) {
            is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = deleteResult.unrecoverableType) }
            is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditTaskUiError.UNKNOWN) }
            is ApiResult.Success -> {
                editorViewModel.setNeedUpdate()
                onGoBack()
            }
        }

        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = CreateEditTaskUiError.NONE) }
    }

}