package online.courseal.courseal_android.ui.viewmodels.editor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.coursemanagement.CoursealCourseManagementService
import online.courseal.courseal_android.data.api.coursemanagement.data.CourseApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.CourseApiResponse
import online.courseal.courseal_android.data.api.coursemanagement.data.CourseListData
import online.courseal.courseal_android.data.api.coursemanagement.data.LessonListApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.LessonListData
import online.courseal.courseal_android.data.api.coursemanagement.data.StructureApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.StructureData
import online.courseal.courseal_android.data.api.coursemanagement.data.TaskListApiError
import online.courseal.courseal_android.data.api.coursemanagement.data.TaskListData
import online.courseal.courseal_android.data.api.usermanagement.CoursealUserManagementService
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

enum class EditorUiError {
    COURSE_NOT_FOUND,
    NO_PERMISSIONS,
    UNKNOWN,
    NONE
}

data class EditorUiState(
    val loading: Boolean = true,
    val needUpdate: Boolean = true,
    val currentCourseId: Int? = null,
    val courses: List<CourseListData>? = null,
    val canCreateCourses: Boolean = false,
    val courseInfo: CourseApiResponse? = null,
    val courseStructure: List<List<StructureData>>? = null,
    val courseLessons: List<LessonListData>? = null,
    val courseTasks: List<TaskListData>? = null,
    val errorState: EditorUiError = EditorUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val userDao: UserDao,
    private val courseManagementService: CoursealCourseManagementService,
    private val userManagementService: CoursealUserManagementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditorUiState())
    val uiState: StateFlow<EditorUiState> = _uiState.asStateFlow()

    private var currentCourseId: Int? by mutableStateOf(null)

    suspend fun update() {
        currentCourseId = userDao.getCurrentUser()?.currentCourseId

        var errorState = EditorUiError.NONE
        var errorUnrecoverableState: UnrecoverableErrorType? = null

        val courses = CoroutineScope(coroutineContext).async {
            when (val coursesResponse = courseManagementService.coursesList()) {
                is ApiResult.UnrecoverableError -> {
                    errorUnrecoverableState = coursesResponse.unrecoverableType
                    null
                }
                is ApiResult.Error -> {
                    errorState = EditorUiError.UNKNOWN
                    null
                }
                is ApiResult.Success -> coursesResponse.successValue
            }
        }

        val userInfo = CoroutineScope(coroutineContext).async {
            when (val userInfoResponse = userManagementService.userInfo()) {
                is ApiResult.UnrecoverableError -> {
                    errorUnrecoverableState = userInfoResponse.unrecoverableType
                    null
                }
                is ApiResult.Error -> {
                    errorState = EditorUiError.UNKNOWN
                    null
                }
                is ApiResult.Success -> userInfoResponse.successValue
            }
        }

        _uiState.update {
            it.copy(
                needUpdate = false,
                currentCourseId = currentCourseId,
                courses = courses.await(),
                canCreateCourses = userInfo.await()?.canCreateCourses ?: false,
                errorState = errorState,
                errorUnrecoverableState = errorUnrecoverableState
            )
        }

        updateCourse()

        _uiState.update {
            it.copy(
                loading = false
            )
        }
    }

    private suspend fun updateCourse() {
        val courseId = currentCourseId

        if (courseId != null) {
            var errorState = EditorUiError.NONE
            var errorUnrecoverableState: UnrecoverableErrorType? = null

            val courseInfo = CoroutineScope(coroutineContext).async {
                when (val courseInfoResponse = courseManagementService.courseInfo(courseId)) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = courseInfoResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = when (courseInfoResponse.errorValue) {
                            CourseApiError.COURSE_NOT_FOUND -> EditorUiError.COURSE_NOT_FOUND
                            CourseApiError.UNKNOWN -> EditorUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.Success -> courseInfoResponse.successValue
                }
            }

            val courseStructure = CoroutineScope(coroutineContext).async {
                when (val courseStructureResponse = courseManagementService.getStructure(courseId)) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = courseStructureResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = when (courseStructureResponse.errorValue) {
                            StructureApiError.NO_PERMISSIONS -> EditorUiError.NO_PERMISSIONS
                            StructureApiError.UNKNOWN -> EditorUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.Success -> courseStructureResponse.successValue
                }
            }

            val courseLessons = CoroutineScope(coroutineContext).async {
                when (val courseLessonsResponse = courseManagementService.lessonsList(courseId)) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = courseLessonsResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = when (courseLessonsResponse.errorValue) {
                            LessonListApiError.NO_PERMISSIONS -> EditorUiError.NO_PERMISSIONS
                            LessonListApiError.UNKNOWN -> EditorUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.Success -> courseLessonsResponse.successValue
                }
            }

            val courseTasks = CoroutineScope(coroutineContext).async {
                when (val courseTasksResponse = courseManagementService.tasksList(courseId)) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = courseTasksResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = when (courseTasksResponse.errorValue) {
                            TaskListApiError.NO_PERMISSIONS -> EditorUiError.NO_PERMISSIONS
                            TaskListApiError.UNKNOWN -> EditorUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.Success -> courseTasksResponse.successValue
                }
            }

            _uiState.update {
                it.copy(
                    courseInfo = courseInfo.await(),
                    courseStructure = courseStructure.await(),
                    courseLessons = courseLessons.await(),
                    courseTasks = courseTasks.await(),
                    errorState = errorState,
                    errorUnrecoverableState = errorUnrecoverableState
                )
            }
        }
    }

    fun setNeedUpdate() {
        _uiState.update {
            it.copy(
                loading = true,
                needUpdate = true
            )
        }
    }

    suspend fun switchCourse(courseId: Int) {
        val currentUser = userDao.getCurrentUser()!!
        userDao.updateUser(currentUser.copy(currentCourseId = courseId))

        _uiState.update {
            it.copy(
                loading = true,
                needUpdate = true
            )
        }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = EditorUiError.NONE) }
    }
}