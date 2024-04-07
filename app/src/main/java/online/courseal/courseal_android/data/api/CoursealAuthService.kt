package online.courseal.courseal_android.data.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.database.dao.ServerDao
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject

@Serializable
data class RegistrationApiRequest(
    val usertag: String,
    val username: String,
    val password: String
)

@Serializable
data class LoginApiRequest(
    val usertag: String,
    val password: String
)

enum class RegistrationApiError {
    USER_EXISTS,
    UNKNOWN
}

enum class LoginApiError {
    INCORRECT,
    UNKNOWN
}

enum class RefreshApiError {
    SUCCESS,
    INVALID,
    UNKNOWN
}

class CoursealAuthService @Inject constructor(
    private val httpClient: HttpClient,
    private val serverDao: ServerDao
) {
    private suspend fun registrationUrl(): String = "${serverDao.getCurrentServerUrl()}/api/user/register"
    private suspend fun loginUrl(): String = "${serverDao.getCurrentServerUrl()!!}/api/auth/login"
    private suspend fun refreshUrl(): String = "${serverDao.getCurrentServerUrl()}/api/auth/refresh"
    private suspend fun logoutUrl(): String = "${serverDao.getCurrentServerUrl()}/api/auth/logout"

    suspend fun register(usertag: String, username: String, password: String): ApiResult<Unit, RegistrationApiError> {
        return try {
            val response = httpClient.post(registrationUrl()) {
                contentType(ContentType.Application.Json)
                setBody(
                    RegistrationApiRequest(
                        usertag = usertag,
                        username = username,
                        password = password
                    )
                )
            }

            if (response.status.value == 200)
                ApiResult.Success(Unit)
            else
                ApiResult.Error(RegistrationApiError.USER_EXISTS)
        }
        catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException ->ApiResult.UnrecoverableError(UnrecoverableErrorType.SERVER_NOT_RESPONDING)
                else ->ApiResult.Error(RegistrationApiError.UNKNOWN)
            }
        }
    }

    suspend fun login(usertag: String, password: String): ApiResult<Unit, LoginApiError> {
        return try {
            val response = httpClient.post(loginUrl()) {
                contentType(ContentType.Application.Json)
                setBody(LoginApiRequest(
                    usertag = usertag,
                    password = password
                ))
            }

            if (response.status.value == 200 || response.status.value == 204)
                ApiResult.Success(Unit)
            else
                ApiResult.Error(LoginApiError.INCORRECT)
        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(UnrecoverableErrorType.SERVER_NOT_RESPONDING)
                else -> ApiResult.Error(LoginApiError.UNKNOWN)
            }
        }
    }

    suspend fun refresh(): ApiResult<Unit, RefreshApiError> {
        return try {
            val response = httpClient.get(refreshUrl())

            if (response.status.value == 200 || response.status.value == 204)
                ApiResult.Success(Unit)
            else
                ApiResult.Error(RefreshApiError.INVALID)
        } catch(e: Exception) {
            when (e) {
                is IOException, is UnresolvedAddressException -> ApiResult.UnrecoverableError(UnrecoverableErrorType.SERVER_NOT_RESPONDING)
                else -> ApiResult.Error(RefreshApiError.UNKNOWN)
            }
        }
    }

}