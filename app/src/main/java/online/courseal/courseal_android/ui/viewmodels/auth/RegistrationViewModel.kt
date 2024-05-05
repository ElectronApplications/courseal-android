package online.courseal.courseal_android.ui.viewmodels.auth

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
import online.courseal.courseal_android.data.api.auth.CoursealAuthService
import online.courseal.courseal_android.data.api.usermanagement.CoursealUserManagementService
import online.courseal.courseal_android.data.api.usermanagement.data.RegistrationApiError
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.util.validateUsertag
import javax.inject.Inject

enum class RegistrationUiError {
    EMPTY_FIELDS,
    INCORRECT,
    USER_EXISTS,
    UNKNOWN,
    NONE
}

data class RegistrationUiState(
    val serverUrl: String = "",
    val serverName: String = "",
    val serverDescription: String = "",
    val usertag: String = "",
    val username: String = "",
    val password: String = "",
    val makingRequest: Boolean = false,
    val errorState: RegistrationUiError = RegistrationUiError.NONE
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val serverDao: ServerDao,
    private val coursealAuthService: CoursealAuthService,
    private val coursealUserManagementService: CoursealUserManagementService
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private var usertag = ""
    private var username = ""
    private var password = ""

    private lateinit var server: Server

    init {
        viewModelScope.launch {
            server = serverDao.findServerById(state["serverId"]!!)!!
            _uiState.update {
                it.copy(
                    serverUrl = server.serverUrl,
                    serverName = server.serverName,
                    serverDescription = server.serverDescription
                )
            }
        }
    }

    fun updateUsertag(usertag: String) {
        if (usertag.isEmpty() || validateUsertag(usertag)) {
            this.usertag = usertag
            _uiState.update { it.copy(usertag = usertag) }
        }
    }

    fun updateUsername(username: String) {
        this.username = username
        _uiState.update { it.copy(username = username) }
    }

    fun updatePassword(password: String) {
        this.password = password
        _uiState.update { it.copy(password = password) }
    }

    fun getServerId(): Long {
        return server.serverId
    }

    suspend fun register(onRegister: () -> Unit, onUnrecoverable: OnUnrecoverable) {
        if (usertag.isEmpty() || username.isEmpty() || password.isEmpty()) {
            _uiState.update { it.copy(errorState = RegistrationUiError.EMPTY_FIELDS) }
            return
        }

        _uiState.update { it.copy(makingRequest = true) }

        if (userDao.findUserByUsertagServer(usertag, server.serverId) != null) {
            _uiState.update {
                it.copy(
                    makingRequest = false,
                    errorState = RegistrationUiError.USER_EXISTS
                )
            }
            return
        }

        val newUserId = userDao.insertUser(User(
            usertag = usertag,
            serverId = server.serverId,
            loggedIn = false
        ))
        userDao.setCurrentUser(newUserId)

        when (val result = coursealUserManagementService.register(usertag, username, password)) {
            is ApiResult.UnrecoverableError -> {
                userDao.deleteUserById(newUserId)
                onUnrecoverable(result.unrecoverableType)
            }

            is ApiResult.Error -> {
                userDao.deleteUserById(newUserId)
                when (result.errorValue) {
                    RegistrationApiError.INCORRECT_USERTAG -> _uiState.update { it.copy(errorState = RegistrationUiError.INCORRECT) }
                    RegistrationApiError.USER_EXISTS -> _uiState.update { it.copy(errorState = RegistrationUiError.USER_EXISTS) }
                    RegistrationApiError.UNKNOWN -> _uiState.update { it.copy(errorState = RegistrationUiError.UNKNOWN) }
                }
            }

            is ApiResult.Success -> {
                when (val loginResult = coursealAuthService.login(usertag, password)) {
                    is ApiResult.UnrecoverableError -> onUnrecoverable(loginResult.unrecoverableType)
                    is ApiResult.Error -> onUnrecoverable(UnrecoverableErrorType.OTHER_UNRECOVERABLE)
                    is ApiResult.Success -> {
                        userDao.setUserLoggedIn(newUserId, true)
                        onRegister()
                    }
                }
            }
        }

        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = RegistrationUiError.NONE) }
    }

}