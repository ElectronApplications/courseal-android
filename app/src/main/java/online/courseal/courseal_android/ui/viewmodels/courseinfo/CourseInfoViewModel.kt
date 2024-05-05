package online.courseal.courseal_android.ui.viewmodels.courseinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.course.CoursealCourseService
import online.courseal.courseal_android.data.api.course.data.CourseApiError
import online.courseal.courseal_android.data.api.course.data.CourseApiResponse
import online.courseal.courseal_android.data.api.courseenrollment.CoursealCourseEnrollmentService
import online.courseal.courseal_android.data.api.courseenrollment.data.CourseEnrollApiError
import javax.inject.Inject

enum class CourseInfoUiError {
    COURSE_NOT_FOUND,
    UNKNOWN,
    NONE
}

data class CourseInfoUiState(
    val loading: Boolean = true,
    val courseInfo: CourseApiResponse? = null,
    val isEnrolled: Boolean? = null,
    val enrolling: Boolean = false,
    val errorState: CourseInfoUiError = CourseInfoUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class CourseInfoViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val courseService: CoursealCourseService,
    private val courseEnrollmentService: CoursealCourseEnrollmentService
) : ViewModel() {
    private val _uiState = MutableStateFlow(CourseInfoUiState())
    val uiState: StateFlow<CourseInfoUiState> = _uiState.asStateFlow()

    var courseId: Int = 0

    init {
        viewModelScope.launch {
            courseId = state["courseId"]!!

            var errorState = CourseInfoUiError.NONE
            var errorUnrecoverableState: UnrecoverableErrorType? = null

            val courseInfo = async {
                when (val courseInfoResponse = courseService.courseInfo(courseId)) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = courseInfoResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = when (courseInfoResponse.errorValue) {
                            CourseApiError.COURSE_NOT_FOUND -> CourseInfoUiError.COURSE_NOT_FOUND
                            CourseApiError.UNKNOWN -> CourseInfoUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.Success -> courseInfoResponse.successValue
                }
            }

            val enrollments = async {
                when (val enrollmentsResponse = courseEnrollmentService.coursesList()) {
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = enrollmentsResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Error -> {
                        errorState = CourseInfoUiError.UNKNOWN
                        null
                    }
                    is ApiResult.Success -> enrollmentsResponse.successValue
                }
            }

            _uiState.update {
                it.copy(
                    loading = false,
                    courseInfo = courseInfo.await(),
                    isEnrolled = enrollments.await()?.any { enrollment -> enrollment.courseId == courseId },
                    errorState = errorState,
                    errorUnrecoverableState = errorUnrecoverableState
                )
            }
        }
    }

    suspend fun enroll() {
        _uiState.update { it.copy(enrolling = true) }
        when (val result = courseEnrollmentService.courseEnroll(courseId)) {
            is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = result.unrecoverableType) }
            is ApiResult.Error -> when (result.errorValue) {
                CourseEnrollApiError.COURSE_NOT_FOUND -> _uiState.update { it.copy(errorState = CourseInfoUiError.COURSE_NOT_FOUND) }
                CourseEnrollApiError.UNKNOWN -> _uiState.update { it.copy(errorState = CourseInfoUiError.UNKNOWN) }
            }
            is ApiResult.Success -> _uiState.update { it.copy(isEnrolled = true) }
        }
        _uiState.update { it.copy(enrolling = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = CourseInfoUiError.NONE) }
    }
}