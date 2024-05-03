package online.courseal.courseal_android.ui.viewmodels.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.user.CoursealUserService
import online.courseal.courseal_android.data.api.user.data.UserListData
import online.courseal.courseal_android.ui.OnUnrecoverable
import javax.inject.Inject

data class SearchUsersUiState(
    val searchTerm: String = "",
    val users: List<UserListData> = emptyList()
)

@HiltViewModel
class SearchUsersViewModel  @Inject constructor(
    private val userService: CoursealUserService
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUsersUiState())
    val uiState: StateFlow<SearchUsersUiState> = _uiState.asStateFlow()

    private var searchTerm = ""

    fun updateSearchTerm(searchTerm: String) {
        this.searchTerm = searchTerm
        _uiState.update { it.copy(searchTerm = searchTerm) }
    }

    suspend fun search(onUnrecoverable: OnUnrecoverable) {
        when (val result = userService.usersList(searchTerm.replace("@", ""))) {
            is ApiResult.UnrecoverableError -> onUnrecoverable(result.unrecoverableType)
            is ApiResult.Error -> {}
            is ApiResult.Success -> _uiState.update { it.copy(users = result.successValue) }
        }
    }
}