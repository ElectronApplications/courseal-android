package online.courseal.courseal_android.ui.viewmodels.profile

import androidx.lifecycle.SavedStateHandle
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
import online.courseal.courseal_android.data.api.user.CoursealUserService
import online.courseal.courseal_android.data.api.user.data.UserActivityApiError
import online.courseal.courseal_android.data.api.user.data.UserActivityApiResponse
import online.courseal.courseal_android.data.api.user.data.UserApiError
import online.courseal.courseal_android.data.api.user.data.UserApiResponse
import online.courseal.courseal_android.data.api.usermanagement.CoursealUserManagementService
import online.courseal.courseal_android.data.api.usermanagement.data.UserManagementApiError
import online.courseal.courseal_android.data.api.usermanagement.data.UserManagementApiResponse
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

enum class ProfileUiError {
    USER_NOT_FOUND,
    UNKNOWN,
    NONE
}

data class ProfileUiState(
    val loading: Boolean = true,
    val needUpdate: Boolean = true,
    val isCurrent: Boolean = false,
    val userPublicInfo: UserApiResponse? = null,
    val userPrivateInfo: UserManagementApiResponse? = null,
    val userActivity: UserActivityApiResponse? = null,
    val errorState: ProfileUiError = ProfileUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val userService: CoursealUserService,
    private val userManagementService: CoursealUserManagementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    suspend fun update() {
        val currentUser = userDao.getCurrentUser()

        val (isCurrent, usertag) = if (state.get<Boolean>("isCurrent")!!) {
            Pair(true, currentUser!!.usertag)
        } else {
            val usertag: String = state["usertag"]!!
            Pair(currentUser?.usertag == usertag, usertag)
        }

        var errorState = ProfileUiError.NONE
        var errorUnrecoverableState: UnrecoverableErrorType? = null

        val userPublicInfo = CoroutineScope(coroutineContext).async {
            when (val userPublicInfoResponse = userService.userInfo(usertag)) {
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
        }

        val userActivity = CoroutineScope(coroutineContext).async {
            when (val userActivityResponse = userService.userActivity(usertag)) {
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
        }

        val userPrivateInfo = CoroutineScope(coroutineContext).async {
            if (isCurrent) {
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
        }

        _uiState.update {
            it.copy(
                loading = false,
                needUpdate = false,
                isCurrent = isCurrent,
                userPublicInfo = userPublicInfo.await(),
                userPrivateInfo = userPrivateInfo.await(),
                userActivity = userActivity.await(),
                errorState = errorState,
                errorUnrecoverableState = errorUnrecoverableState
            )
        }
    }

    fun setNeedUpdate() {
        _uiState.update { it.copy(needUpdate = true) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = ProfileUiError.NONE) }
    }
}