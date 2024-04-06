package online.courseal.courseal_android.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import online.courseal.courseal_android.data.api.CoursealServerService
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.entities.Server
import javax.inject.Inject

const val DEFAULT_SERVER = "https://courseal.online"

data class WelcomeUiState(
    val serverUrl: String = DEFAULT_SERVER,
    val providedUrl: String = "",
    val makingRequest: Boolean = false,
    val showError: Boolean = false,
)

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val serverDao: ServerDao,
    private val coursealServerService: CoursealServerService
) : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private var serverUrl: String by mutableStateOf(DEFAULT_SERVER)

    fun updateUrl(providedUrl: String) {
        this.serverUrl = providedUrl.ifEmpty { DEFAULT_SERVER }
        _uiState.update {
            it.copy(
                providedUrl = providedUrl,
                serverUrl = this.serverUrl
            )
        }
    }

    suspend fun start(onStart: (serverRegistrationEnabled: Boolean, serverId: Long) -> Unit) {
        val serverUrl = this.serverUrl
        _uiState.update { it.copy(makingRequest = true) }

        val serverInfo = coursealServerService.coursealInfo(serverUrl)

        _uiState.update {
            it.copy(
                makingRequest = false,
                showError = serverInfo == null
            )
        }

        if (serverInfo != null) {
            val oldServer = serverDao.findServerByUrl(serverUrl)
            val newServerId = serverDao.upsertServer(Server(
                serverId = oldServer?.serverId ?: 0,
                serverUrl = serverUrl,
                serverName = serverInfo.serverName,
                serverDescription = serverInfo.serverDescription,
                serverRegistrationEnabled = serverInfo.serverRegistrationEnabled
            ))

            onStart(serverInfo.serverRegistrationEnabled, oldServer?.serverId ?: newServerId)
        }
    }

    fun hideDialog() {
        _uiState.update { it.copy(showError = false) }
    }

}