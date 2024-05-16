package online.courseal.courseal_android.ui.viewmodels.courseinfo

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.course.CoursealCourseService
import online.courseal.courseal_android.data.api.course.data.CourseListData
import online.courseal.courseal_android.ui.OnUnrecoverable
import javax.inject.Inject

data class SearchCoursesUiState(
    val searchTerm: String = "",
    val courses: List<CourseListData> = emptyList()
)

@HiltViewModel
class SearchCoursesViewModel @Inject constructor(
    private val courseService: CoursealCourseService
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchCoursesUiState())
    val uiState: StateFlow<SearchCoursesUiState> = _uiState.asStateFlow()

    private var searchTerm = ""

    fun updateSearchTerm(searchTerm: String) {
        this.searchTerm = searchTerm
        _uiState.update { it.copy(searchTerm = searchTerm) }
    }

    suspend fun search(onUnrecoverable: OnUnrecoverable) {
        when (val result = courseService.coursesList(searchTerm)) {
            is ApiResult.UnrecoverableError -> onUnrecoverable(result.unrecoverableType)
            is ApiResult.Error -> {}
            is ApiResult.Success -> _uiState.update { it.copy(courses = result.successValue) }
        }
    }

}