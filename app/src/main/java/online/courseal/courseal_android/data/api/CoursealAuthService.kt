package online.courseal.courseal_android.data.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import online.courseal.courseal_android.data.database.dao.ServerDao
import javax.inject.Inject

@Serializable
data class RegistrationRequest(
    val usertag: String,
    val username: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val usertag: String,
    val password: String
)

enum class RegistrationResult {
    SUCCESS,
    USER_EXISTS,
    UNKNOWN
}

enum class LoginResult {
    SUCCESS,
    INCORRECT,
    UNKNOWN
}

enum class RefreshResult {
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

    suspend fun register(usertag: String, username: String, password: String): RegistrationResult {
        return try {
            val response = httpClient.post(registrationUrl()) {
                contentType(ContentType.Application.Json)
                setBody(RegistrationRequest(
                    usertag = usertag,
                    username = username,
                    password = password
                ))
            }

            if (response.status.value == 200)
                RegistrationResult.SUCCESS
            else
                RegistrationResult.USER_EXISTS
        } catch(_: Exception) {
            RegistrationResult.UNKNOWN
        }
    }

    suspend fun login(usertag: String, password: String): LoginResult {
        return try {
            val response = httpClient.post(loginUrl()) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(
                    usertag = usertag,
                    password = password
                ))
            }

            if (response.status.value == 200 || response.status.value == 204)
                LoginResult.SUCCESS
            else
                LoginResult.INCORRECT
        } catch(_: Exception) {
            LoginResult.UNKNOWN
        }
    }

    suspend fun refresh(): RefreshResult {
        return try {
            val response = httpClient.get(refreshUrl())

            if (response.status.value == 200 || response.status.value == 204)
                RefreshResult.SUCCESS
            else
                RefreshResult.INVALID
        } catch(_: Exception) {
            RefreshResult.UNKNOWN
        }
    }

}