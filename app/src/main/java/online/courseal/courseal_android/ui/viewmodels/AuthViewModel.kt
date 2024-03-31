package online.courseal.courseal_android.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import online.courseal.courseal_android.data.api.ServerInfo
import online.courseal.courseal_android.data.api.coursealInfo

const val DEFAULT_SERVER = "https://courseal.online"

data class AuthUiState(
    val serverUrl: String = DEFAULT_SERVER,
    val serverName: String = "",
    val serverDescription: String = "",
    val serverRegistrationEnabled: Boolean = true
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private var serverUrl: String by mutableStateOf(DEFAULT_SERVER)
    private var serverInfo: ServerInfo? by mutableStateOf(null)

    fun updateUrl(userUrl: String) {
        this.serverUrl = userUrl.ifEmpty { DEFAULT_SERVER }
        _uiState.update {
            it.copy(
                serverUrl = this.serverUrl
            )
        }
    }

    suspend fun getServerInfo(): Boolean {
        val serverUrl = this.serverUrl
        val serverInfo = coursealInfo(serverUrl)

        if (serverInfo != null) {
            this.serverUrl = serverUrl
            this.serverInfo = serverInfo
            _uiState.update {
                it.copy(
                    serverName = serverInfo.serverName,
                    serverDescription = serverInfo.serverDescription,
                    serverRegistrationEnabled = serverInfo.serverRegistrationEnabled
                )
            }
        }

        return serverInfo != null
    }

}