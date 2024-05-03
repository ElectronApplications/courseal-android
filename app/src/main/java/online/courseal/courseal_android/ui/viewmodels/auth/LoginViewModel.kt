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
import online.courseal.courseal_android.data.api.auth.data.LoginApiError
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import online.courseal.courseal_android.ui.OnUnrecoverable
import online.courseal.courseal_android.ui.util.validateUsertag
import javax.inject.Inject

enum class LoginUiError {
    INCORRECT,
    UNKNOWN,
    NONE
}

data class LoginUiState(
    val serverUrl: String = "",
    val serverName: String = "",
    val serverDescription: String = "",
    val usertagEditable: Boolean = true,
    val usertag: String = "",
    val password: String = "",
    val makingRequest: Boolean = false,
    val errorState: LoginUiError = LoginUiError.NONE
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val serverDao: ServerDao,
    private val coursealAuthService: CoursealAuthService
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var usertag = ""
    private var password = ""

    private lateinit var server: Server
    private var currentUserId: Long? = null

    init {
        viewModelScope.launch {
            val currentUser = userDao.getCurrentUser()
            if (currentUser != null) {
                usertag = currentUser.usertag
                currentUserId = currentUser.userId
            }

            server = serverDao.findServerById(state["serverId"]!!)!!
            _uiState.update {
                it.copy(
                    usertag = currentUser?.usertag ?: "",
                    usertagEditable = currentUser == null,
                    serverUrl = server.serverUrl,
                    serverName = server.serverName,
                    serverDescription = server.serverDescription
                )
            }
        }
    }

    fun updateUsertag(usertag: String) {
        if (currentUserId == null && (usertag.isEmpty() || validateUsertag(usertag))) {
            this.usertag = usertag
            _uiState.update { it.copy(usertag = usertag) }
        }
    }

    fun updatePassword(password: String) {
        this.password = password
        _uiState.update { it.copy(password = password) }
    }

    suspend fun login(onLogin: () -> Unit, onUnrecoverable: OnUnrecoverable) {
        _uiState.update { it.copy(makingRequest = true) }

        val userId = currentUserId ?: userDao.insertUser(User(
            usertag = usertag,
            serverId = server.serverId,
            loggedIn = false
        ))
        userDao.setCurrentUser(userId)

        when (val result = coursealAuthService.login(usertag, password)) {
            is ApiResult.UnrecoverableError -> {
                if (currentUserId == null)
                    userDao.deleteUserById(userId)
                onUnrecoverable(result.unrecoverableType)
            }

            is ApiResult.Error -> {
                if (currentUserId == null)
                    userDao.deleteUserById(userId)
                when (result.errorValue) {
                    LoginApiError.INCORRECT -> _uiState.update { it.copy(errorState = LoginUiError.INCORRECT) }
                    LoginApiError.UNKNOWN -> _uiState.update { it.copy(errorState = LoginUiError.UNKNOWN) }
                }
            }

            is ApiResult.Success -> {
                userDao.setUserLoggedIn(userId, true)
                onLogin()
            }
        }

        _uiState.update { it.copy(makingRequest = false) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = LoginUiError.NONE) }
    }

}