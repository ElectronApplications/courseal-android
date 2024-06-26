package online.courseal.courseal_android.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.ui.NavBarOptions
import online.courseal.courseal_android.ui.NavItems
import online.courseal.courseal_android.ui.Routes
import javax.inject.Inject

enum class TopLevelUiError {
    REFRESH_INVALID,
    SERVER_NOT_RESPONDING,
    OTHER_UNRECOVERABLE,
    NONE
}

data class TopLevelUiState(
    val isLoading: Boolean = true,
    val startDestination: Routes = Routes.WELCOME,
    val navBarShown: Boolean = false,
    val selectedNavEntry: NavItems? = null,
    val errorState: TopLevelUiError = TopLevelUiError.NONE
)

@HiltViewModel
class TopLevelViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(TopLevelUiState())
    val uiState: StateFlow<TopLevelUiState> = _uiState.asStateFlow()

    private var startDestination = Routes.WELCOME
    init {
        viewModelScope.launch {
            setLoading(true)
            setStartingDestination()
            setLoading(false)
        }
    }

    private suspend fun setStartingDestination(): Routes {
        val currentUser = userDao.getCurrentUser()
        val users = userDao.getAllUsers()

        startDestination = if (currentUser != null && currentUser.loggedIn) {
            Routes.COURSE
        } else if (users.isNotEmpty()) {
            Routes.ACCOUNTS
        } else {
            Routes.WELCOME
        }

        _uiState.update { it.copy(startDestination = startDestination) }
        return startDestination
    }

    suspend fun processUnrecoverable(errorType: UnrecoverableErrorType): Routes {
        setLoading(true)

        _uiState.update {
            it.copy(
                errorState = when (errorType) {
                    UnrecoverableErrorType.REFRESH_INVALID -> TopLevelUiError.REFRESH_INVALID
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING -> TopLevelUiError.SERVER_NOT_RESPONDING
                    UnrecoverableErrorType.OTHER_UNRECOVERABLE -> TopLevelUiError.OTHER_UNRECOVERABLE
                }
            )
        }

        if (errorType == UnrecoverableErrorType.REFRESH_INVALID
            || errorType == UnrecoverableErrorType.SERVER_NOT_RESPONDING) {
            userDao.setCurrentUser(null)
        }

        val destination = setStartingDestination()
        setLoading(false)
        return destination
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.update { it.copy(isLoading = isLoading) }
    }

    fun hideError() {
        _uiState.update { it.copy(errorState = TopLevelUiError.NONE) }
    }

    fun selectNavBarEntry(entry: NavItems, navController: NavHostController) {
        _uiState.update { it.copy(selectedNavEntry = entry) }
        when (entry) {
            NavItems.COURSE -> navController.navigate(Routes.COURSE.path) {
                popUpTo(0) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            NavItems.PROFILE -> navController.navigate("${Routes.PROFILE.path}?isCurrent=true&navBar=${NavBarOptions.SHOW.value}&transitionFade=true") {
                popUpTo(0) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            NavItems.EDITOR -> navController.navigate(Routes.EDITOR.path) {
                popUpTo(0) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    fun setNavBarShown(shown: Boolean) {
        if (shown != uiState.value.navBarShown) {
            _uiState.update {
                it.copy(
                    navBarShown = shown,
                    selectedNavEntry = if (shown) NavItems.COURSE else null
                )
            }
        }
    }

}