package online.courseal.courseal_android.ui.viewmodels.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.usermanagement.CoursealUserManagementService
import online.courseal.courseal_android.data.api.usermanagement.data.ChangePasswordApiError
import javax.inject.Inject

enum class SettingsPasswordUiError {
    PASSWORD_INVALID,
    BAD_REQUEST,
    UNKNOWN,
    NONE
}

data class SettingsPasswordUiState(
    val oldPassword: String = "",
    val newPassword: String = "",
    val makingRequest: Boolean = false,
    val errorState: SettingsPasswordUiError = SettingsPasswordUiError.NONE,
    val errorUnrecoverableState: UnrecoverableErrorType? = null
)

@HiltViewModel
class SettingsPasswordViewModel @Inject constructor(
    private val userManagementService: CoursealUserManagementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsPasswordUiState())
    val uiState: StateFlow<SettingsPasswordUiState> = _uiState.asStateFlow()

    private var oldPassword by mutableStateOf("")
    private var newPassword by mutableStateOf("")

    fun updateOldPassword(oldPassword: String) {
        this.oldPassword = oldPassword
        _uiState.update { it.copy(oldPassword = oldPassword) }
    }

    fun updateNewPassword(newPassword: String) {
        this.newPassword = newPassword
        _uiState.update { it.copy(newPassword = newPassword) }
    }

    suspend fun confirm(onGoBack: () -> Unit) {
        _uiState.update { it.copy(makingRequest = true) }
        when (val result = userManagementService.changePassword(oldPassword, newPassword)) {
            is ApiResult.UnrecoverableError -> _uiState.update { it.copy(errorUnrecoverableState = result.unrecoverableType) }
            is ApiResult.Error -> when (result.errorValue) {
                ChangePasswordApiError.BAD_REQUEST -> _uiState.update { it.copy(errorState = SettingsPasswordUiError.BAD_REQUEST) }
                ChangePasswordApiError.PASSWORD_INVALID -> _uiState.update { it.copy(errorState = SettingsPasswordUiError.PASSWORD_INVALID) }
                ChangePasswordApiError.UNKNOWN -> _uiState.update { it.copy(errorState = SettingsPasswordUiError.UNKNOWN) }
            }
            is ApiResult.Success -> onGoBack()
        }
        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = SettingsPasswordUiError.NONE) }
    }
}