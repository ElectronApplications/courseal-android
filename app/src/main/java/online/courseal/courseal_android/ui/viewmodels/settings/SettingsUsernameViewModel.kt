package online.courseal.courseal_android.ui.viewmodels.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import online.courseal.courseal_android.data.api.usermanagement.CoursealUserManagementService
import online.courseal.courseal_android.data.api.usermanagement.data.ChangeNameApiError
import online.courseal.courseal_android.ui.viewmodels.profile.ProfileViewModel
import javax.inject.Inject

enum class SettingsUsernameUiError {
    BAD_REQUEST,
    UNKNOWN,
    NONE
}

data class SettingsUsernameUiState(
    val username: String = "",
    val makingRequest: Boolean = false,
    val errorState: SettingsUsernameUiError = SettingsUsernameUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class SettingsUsernameViewModel @Inject constructor(
    private val userManagementService: CoursealUserManagementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUsernameUiState())
    val uiState: StateFlow<SettingsUsernameUiState> = _uiState.asStateFlow()

    private var username by mutableStateOf("")

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(makingRequest = true) }

            when (val userInfo = userManagementService.userInfo()) {
                is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = userInfo.unrecoverableType) }
                is ApiResult.Error -> _uiState.update { it.copy(errorState = SettingsUsernameUiError.UNKNOWN) }
                is ApiResult.Success -> username = userInfo.successValue.username
            }

            _uiState.update {
                it.copy(
                    username = username,
                    makingRequest = false
                )
            }
        }
    }

    fun updateUsername(username: String) {
        this.username = username
        _uiState.update { it.copy(username = username) }
    }

    suspend fun confirm(onGoBack: () -> Unit, profileViewModel: ProfileViewModel) {
        _uiState.update { it.copy(makingRequest = true) }
        when (val result = userManagementService.changeUsername(username)) {
            is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = result.unrecoverableType) }
            is ApiResult.Error -> when (result.errorValue) {
                ChangeNameApiError.BAD_REQUEST -> _uiState.update { it.copy(errorState = SettingsUsernameUiError.BAD_REQUEST) }
                ChangeNameApiError.UNKNOWN -> _uiState.update { it.copy(errorState = SettingsUsernameUiError.UNKNOWN) }
            }
            is ApiResult.Success -> {
                onGoBack()
                profileViewModel.update()
            }
        }
        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = SettingsUsernameUiError.NONE) }
    }
}