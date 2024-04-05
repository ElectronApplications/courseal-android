package online.courseal.courseal_android.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.api.LoginResult
import online.courseal.courseal_android.data.api.coursealLogin
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.Server
import javax.inject.Inject

data class LoginUiState(
    val serverUrl: String = "",
    val serverName: String = "",
    val serverDescription: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val userDao: UserDao,
    private val serverDao: ServerDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private lateinit var server: Server

    init {
        viewModelScope.launch {
            server = serverDao.findServerById(state["serverId"]!!)
            _uiState.update {
                it.copy(
                    serverUrl = server.serverUrl,
                    serverName = server.serverName,
                    serverDescription = server.serverDescription
                )
            }
        }
    }

    suspend fun login(usertag: String, password: String, onLogin: () -> Unit) {
        val result = coursealLogin(server.serverUrl, usertag, password)
        if (result == LoginResult.SUCCESS)
            onLogin()
    }

}