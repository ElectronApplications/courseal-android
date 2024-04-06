package online.courseal.courseal_android.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.api.CoursealAuthService
import online.courseal.courseal_android.data.api.LoginResult
import online.courseal.courseal_android.data.api.RegistrationResult
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import online.courseal.courseal_android.ui.logic.validateUsertag
import javax.inject.Inject

enum class RegistrationError {
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
    val errorState: RegistrationError = RegistrationError.NONE
)

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val serverDao: ServerDao,
    private val coursealAuthService: CoursealAuthService
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private var usertag by mutableStateOf("")
    private var username by mutableStateOf("")
    private var password by mutableStateOf("")

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

    suspend fun register(onRegister: () -> Unit) {
        _uiState.update { it.copy(makingRequest = true) }

        if (userDao.findUserByUsertagServer(usertag, server.serverId) != null) {
            _uiState.update {
                it.copy(
                    makingRequest = false,
                    errorState = RegistrationError.USER_EXISTS
                )
            }
            return;
        }

        val newUserId = userDao.insertUser(User(
            usertag = usertag,
            serverId = server.serverId
        ))
        userDao.setCurrentUser(newUserId)

        val result = coursealAuthService.register(usertag, username, password)

        // Remove the user if couldn't register
        if (result != RegistrationResult.SUCCESS) {
            userDao.deleteUserById(newUserId)
            _uiState.update { it.copy(makingRequest = false) }
        }

        when (result) {
            RegistrationResult.SUCCESS -> {
                coursealAuthService.login(usertag, password)
                _uiState.update { it.copy(makingRequest = false) }
                onRegister()
            }
            RegistrationResult.USER_EXISTS -> _uiState.update { it.copy(errorState = RegistrationError.USER_EXISTS) }
            RegistrationResult.UNKNOWN -> _uiState.update { it.copy(errorState = RegistrationError.UNKNOWN) }
        }

    }

    fun hideError() {
        _uiState.update { it.copy(errorState = RegistrationError.NONE) }
    }

}