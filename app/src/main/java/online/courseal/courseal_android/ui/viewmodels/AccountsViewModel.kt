package online.courseal.courseal_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import javax.inject.Inject

data class AccountUi(
    val userId: Long,
    val usertag: String,
    val loggedIn: Boolean,
    val serverUrl: String
)

data class AccountsUiState(
    val accounts: List<AccountUi> = emptyList()
)

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val userDao: UserDao,
    private val serverDao: ServerDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(AccountsUiState())
    val uiState: StateFlow<AccountsUiState> = _uiState.asStateFlow()

    private var accounts = mutableListOf<AccountUi>()

    init {
        viewModelScope.launch {
            val users = userDao.getAllUsers()
            accounts.addAll(users.map { AccountUi(
                userId = it.userId,
                usertag = it.usertag,
                loggedIn = it.loggedIn,
                serverUrl = serverDao.findServerById(it.serverId)!!.serverUrl
            ) })
            _uiState.update { it.copy(accounts = accounts) }
        }
    }

    suspend fun chooseAccount(userId: Long, onLoggedIn: () -> Unit, onNotLoggedIn: (serverId: Long) -> Unit) {
        val account = accounts.first { it.userId == userId }
        userDao.setCurrentUser(account.userId)
        if (account.loggedIn) {
            onLoggedIn()
        } else {
            onNotLoggedIn(serverDao.getCurrentServer()!!.serverId)
        }
    }

    suspend fun removeAccount(userId: Long, onAllAccountsDeleted: () -> Unit) {
        userDao.deleteUserById(userId)
        accounts.removeIf { it.userId == userId }
        _uiState.update { it.copy(accounts = accounts) }

        if (accounts.isEmpty())
            onAllAccountsDeleted()
    }
}