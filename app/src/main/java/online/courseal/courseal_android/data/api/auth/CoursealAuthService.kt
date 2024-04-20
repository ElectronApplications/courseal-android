package online.courseal.courseal_android.data.api.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import online.courseal.courseal_android.data.api.ApiResult
import online.courseal.courseal_android.data.api.ErrorResponse
import online.courseal.courseal_android.data.api.ErrorResponseType
import online.courseal.courseal_android.data.api.UnrecoverableErrorType
import online.courseal.courseal_android.data.api.mapErr
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

class CoursealAuthService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao,
    private val userDao: UserDao
) {
    private suspend fun loginUrl(): String = "${serverDao.getCurrentServerUrl()!!}/api/auth/login"
    private suspend fun refreshUrl(): String = "${serverDao.getCurrentServerUrl()}/api/auth/refresh"
    private suspend fun logoutUrl(): String = "${serverDao.getCurrentServerUrl()}/api/auth/logout"

    suspend fun login(usertag: String, password: String): ApiResult<Unit, LoginApiError> {
        return try {
            val response = httpClient.post(loginUrl()) {
                contentType(ContentType.Application.Json)
                setBody(LoginApiRequest(
                    usertag = usertag,
                    password = password
                ))
            }

            if (response.status.value in 200..299)
                ApiResult.Success(Unit)
            else when(response.body<ErrorResponse>().error) {
                ErrorResponseType.INCORRECT_LOGIN -> ApiResult.Error(LoginApiError.INCORRECT)
                else -> ApiResult.Error(LoginApiError.UNKNOWN)
            }
        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(LoginApiError.UNKNOWN)
            }
        }
    }

    private suspend fun refresh(): ApiResult<Unit, RefreshApiError> {
        return try {
            val response = httpClient.get(refreshUrl())

            if (response.status.value in 200..299)
                ApiResult.Success(Unit)
            else when(response.body<ErrorResponse>().error) {
                ErrorResponseType.REFRESH_INVALID -> ApiResult.Error(RefreshApiError.INVALID)
                else -> ApiResult.Error(RefreshApiError.UNKNOWN)
            }

        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(RefreshApiError.UNKNOWN)
            }
        }
    }

    suspend fun logout(): ApiResult<Unit, LogoutApiError> {
        return try {
            val response = httpClient.get(logoutUrl())

            if (response.status.value in 200..299)
                ApiResult.Success(Unit)
            else when(response.body<ErrorResponse>().error) {
                ErrorResponseType.REFRESH_INVALID -> ApiResult.Error(LogoutApiError.REFRESH_INVALID)
                else -> ApiResult.Error(LogoutApiError.UNKNOWN)
            }

        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(
                    UnrecoverableErrorType.SERVER_NOT_RESPONDING
                )
                else -> ApiResult.Error(LogoutApiError.UNKNOWN)
            }
        }
    }

    suspend fun<T, E> authWrap(inner: suspend () -> ApiResult<T, AuthWrapperError<E>>): ApiResult<T, E> {
        val currentUser = userDao.getCurrentUser()
            ?: return ApiResult.UnrecoverableError(UnrecoverableErrorType.OTHER_UNRECOVERABLE)

        // If the first try is not successful we refresh the JWT and try again
        // If it doesn't work the second time then the refresh token is probably invalid
        for (i in 0 until 2) {
            val resultInner = inner()

            return if (resultInner !is ApiResult.Error || resultInner.errorValue is AuthWrapperError.InnerError) {
                resultInner.mapErr { (it as AuthWrapperError.InnerError).innerError }
            } else {
                when (val resultRefresh = refresh()) {
                    is ApiResult.UnrecoverableError -> ApiResult.UnrecoverableError(resultRefresh.unrecoverableType)
                    is ApiResult.Error -> when (resultRefresh.errorValue) {
                        RefreshApiError.INVALID -> break
                        RefreshApiError.UNKNOWN -> continue // Still not sure if the refresh token is invalid
                    }

                    is ApiResult.Success -> continue // Go for the second try
                }
            }
        }

        userDao.setUserLoggedIn(currentUser.userId, false)
        return ApiResult.UnrecoverableError(UnrecoverableErrorType.REFRESH_INVALID)
    }

}