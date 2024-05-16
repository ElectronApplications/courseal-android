package online.courseal.courseal_android.ui.viewmodels.course

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.courseenrollment.CoursealCourseEnrollmentService
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollInfoApiError
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollInfoApiResponse
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseListData
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

enum class CourseUiError {
    COURSE_NOT_FOUND,
    UNKNOWN,
    NONE
}

data class CourseUiState(
    val needUpdate: Boolean = true,
    val loading: Boolean = true,
    val coursesList: List<CourseListData>? = null,
    val courseInfo: CourseEnrollInfoApiResponse? = null,
    val errorState: CourseUiError = CourseUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val userDao: UserDao,
    private val courseEnrollmentService: CoursealCourseEnrollmentService
) : ViewModel() {
    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState: StateFlow<CourseUiState> = _uiState.asStateFlow()

    private var currentCourseId: Int? = null

    suspend fun update() {
        currentCourseId = userDao.getCurrentUser()?.currentCourseId

        var errorState = CourseUiError.NONE
        var errorUnrecoverableState: UnrecoverableErrorType? = null

        val courses = CoroutineScope(coroutineContext).async {
            when (val coursesResponse = courseEnrollmentService.coursesList()) {
                is ApiResult.UnrecoverableError -> {
                    errorUnrecoverableState = coursesResponse.unrecoverableType
                    null
                }
                is ApiResult.Error -> {
                    errorState = CourseUiError.UNKNOWN
                    null
                }
                is ApiResult.Success -> coursesResponse.successValue
            }
        }

        val currentCourseInfo = CoroutineScope(coroutineContext).async {
            currentCourseId?.let { courseId ->
                when (val courseInfoResponse = courseEnrollmentService.courseInfo(courseId)) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = courseInfoResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = when (courseInfoResponse.errorValue) {
                            CourseEnrollInfoApiError.COURSE_NOT_FOUND -> CourseUiError.COURSE_NOT_FOUND
                            CourseEnrollInfoApiError.UNKNOWN -> CourseUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.Success -> courseInfoResponse.successValue
                }
            }
        }

        _uiState.update {
            it.copy(
                needUpdate = false,
                loading = false,
                coursesList = courses.await(),
                courseInfo = currentCourseInfo.await(),
                errorState = errorState,
                errorUnrecoverableState = errorUnrecoverableState
            )
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
        _uiState.update { it.copy(errorState = CourseUiError.NONE) }
    }
}