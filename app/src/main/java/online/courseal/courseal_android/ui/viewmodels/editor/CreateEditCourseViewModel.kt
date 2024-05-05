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
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.coursemanagement.CoursealCourseManagementService
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject
import kotlin.properties.Delegates

enum class CreateEditCourseUiError {
    UNKNOWN,
    NONE
}

data class CreateEditCourseUiState(
    val makingRequest: Boolean = true,
    val loading: Boolean = true,
    val isCreating: Boolean = true,
    val courseId: Int? = null,
    val courseName: String = "",
    val courseDescription: String = "",
    val errorState: CreateEditCourseUiError = CreateEditCourseUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class CreateEditCourseViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val courseManagementService: CoursealCourseManagementService,
    private val userDao: UserDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateEditCourseUiState())
    val uiState: StateFlow<CreateEditCourseUiState> = _uiState.asStateFlow()

    private var isCreating by Delegates.notNull<Boolean>()
    private var courseId: Int? = null

    private var courseName = ""
    private var courseDescription = ""

    init {
        viewModelScope.launch {
            isCreating = state["isCreating"]!!

            var errorState: CreateEditCourseUiError = CreateEditCourseUiError.NONE
            var errorUnrecoverableState: UnrecoverableErrorType? = null

            if (!isCreating) {
                courseId = state["courseId"]!!
                when (val courseInfo = courseManagementService.courseInfo(courseId!!)) {
                    is ApiResult.UnrecoverableError -> errorUnrecoverableState = courseInfo.unrecoverableType
                    is ApiResult.Error -> errorState = CreateEditCourseUiError.UNKNOWN
                    is ApiResult.Success -> {
                        courseName = courseInfo.successValue.courseName
                        courseDescription = courseInfo.successValue.courseDescription
                    }
                }
            }

            _uiState.update {
                it.copy(
                    makingRequest = false,
                    loading = false,
                    isCreating = isCreating,
                    courseId = courseId,
                    courseName = courseName,
                    courseDescription = courseDescription,
                    errorState = errorState,
                    errorUnrecoverableState = errorUnrecoverableState
                )
            }
        }
    }

    fun updateCourseName(courseName: String) {
        this.courseName = courseName
        _uiState.update { it.copy(courseName = courseName) }
    }

    fun updateCourseDescription(courseDescription: String) {
        this.courseDescription = courseDescription
        _uiState.update { it.copy(courseDescription = courseDescription) }
    }

    suspend fun confirm(onGoBack: () -> Unit, editorViewModel: EditorViewModel) {
        _uiState.update { it.copy(makingRequest = true) }

        if (isCreating) {
            when (val createResult = courseManagementService.createCourse(courseName, courseDescription)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = createResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditCourseUiError.UNKNOWN) }
                is ApiResult.Success -> {
                    val currentUser = userDao.getCurrentUser()!!
                    userDao.updateUser(currentUser.copy(currentEditorCourseId = createResult.successValue.courseId))
                    editorViewModel.setNeedUpdate()
                    onGoBack()
                }
            }
        } else {
            when (val updateResult = courseManagementService.updateCourseInfo(courseId!!, courseName, courseDescription)) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = updateResult.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditCourseUiError.UNKNOWN) }
                is ApiResult.Success -> {
                    editorViewModel.setNeedUpdate()
                    onGoBack()
                }
            }
        }

        _uiState.update { it.copy(makingRequest = false) }
    }

    suspend fun delete(onGoBack: () -> Unit, editorViewModel: EditorViewModel) {
        _uiState.update { it.copy(makingRequest = true) }

        when (val deleteResult = courseManagementService.deleteCourse(courseId!!)) {
            is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = deleteResult.unrecoverableType) }
            is ApiResult.Error -> _uiState.update { it.copy(errorState = CreateEditCourseUiError.UNKNOWN) }
            is ApiResult.Success -> {
                if (courseId == editorViewModel.uiState.value.currentCourseId) {
                    val currentUser = userDao.getCurrentUser()!!
                    userDao.updateUser(currentUser.copy(currentEditorCourseId = null))
                }
                editorViewModel.setNeedUpdate()
                onGoBack()
            }
        }
        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = CreateEditCourseUiError.NONE) }
    }
}