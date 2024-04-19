package online.courseal.courseal_android.ui.viewmodels

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
import online.courseal.courseal_android.data.api.user.CoursealUserService
import online.courseal.courseal_android.data.api.user.UserActivityApiError
import online.courseal.courseal_android.data.api.user.UserActivityApiResponse
import online.courseal.courseal_android.data.api.user.UserApiError
import online.courseal.courseal_android.data.api.user.UserApiResponse
import online.courseal.courseal_android.data.api.usermanagement.CoursealUserManagementService
import online.courseal.courseal_android.data.api.usermanagement.UserManagementApiError
import online.courseal.courseal_android.data.api.usermanagement.UserManagementApiResponse
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject

enum class ProfileUiError {
    USER_NOT_FOUND,
    UNKNOWN,
    NONE
}

data class ProfileScreenUiState(
    val loading: Boolean = true,
    val isCurrent: Boolean = false,
    val userPublicInfo: UserApiResponse? = null,
    val userPrivateInfo: UserManagementApiResponse? = null,
    val userActivity: UserActivityApiResponse? = null,
    val errorState: ProfileUiError = ProfileUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val userService: CoursealUserService,
    private val userManagementService: CoursealUserManagementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState: StateFlow<ProfileScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val usertagParam: String? = state["usertag"]

            val (usertag, isCurrent) = if (usertagParam == null) {
                val currentUser = userDao.getCurrentUser()
                Pair(currentUser?.usertag ?: "", currentUser != null)
            } else Pair(usertagParam, false)

            var errorState = ProfileUiError.NONE
            var errorUnrecoverableState: UnrecoverableErrorType? = null

            val userPublicInfo = when (val userPublicInfoResponse = userService.userInfo(usertag)) {
                is ApiResult.UnrecoverableError -> {
                    errorUnrecoverableState = userPublicInfoResponse.unrecoverableType
                    null
                }
                is ApiResult.Error -> {
                    errorState = when (userPublicInfoResponse.errorValue) {
                        UserApiError.USER_NOT_FOUND -> ProfileUiError.USER_NOT_FOUND
                        UserApiError.UNKNOWN -> ProfileUiError.UNKNOWN
                    }
                    null
                }
                is ApiResult.Success -> userPublicInfoResponse.successValue
            }


            val userActivity = when (val userActivityResponse = userService.userActivity(usertag)) {
                is ApiResult.Error -> {
                    errorState = when (userActivityResponse.errorValue) {
                        UserActivityApiError.USER_NOT_FOUND -> ProfileUiError.USER_NOT_FOUND
                        UserActivityApiError.UNKNOWN -> ProfileUiError.UNKNOWN
                    }
                    null
                }
                is ApiResult.UnrecoverableError -> {
                    errorUnrecoverableState = userActivityResponse.unrecoverableType
                    null
                }
                is ApiResult.Success -> userActivityResponse.successValue
            }

            val userPrivateInfo = if (isCurrent) {
                when (val userPrivateInfoResponse = userManagementService.userInfo()) {
                    is ApiResult.Error -> {
                        errorState = when (userPrivateInfoResponse.errorValue) {
                            UserManagementApiError.UNKNOWN -> ProfileUiError.UNKNOWN
                        }
                        null
                    }
                    is ApiResult.UnrecoverableError -> {
                        errorUnrecoverableState = userPrivateInfoResponse.unrecoverableType
                        null
                    }
                    is ApiResult.Success -> userPrivateInfoResponse.successValue
                }
            } else null

            _uiState.update {
                it.copy(
                    loading = false,
                    isCurrent = isCurrent,
                    userPublicInfo = userPublicInfo,
                    userPrivateInfo = userPrivateInfo,
                    userActivity = userActivity,
                    errorState = errorState,
                    errorUnrecoverableState = errorUnrecoverableState
                )
            }
        }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = ProfileUiError.NONE) }
    }
}