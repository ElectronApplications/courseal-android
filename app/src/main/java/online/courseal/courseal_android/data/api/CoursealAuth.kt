package online.courseal.courseal_android.data.api

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val usertag: String,
    val password: String
)

enum class LoginResult {
    SUCCESS,
    INCORRECT,
    UNKNOWN
}

suspend fun coursealLogin(url: String, usertag: String, password: String): LoginResult {
    return try {
        val response = httpClient.post("$url/api/auth/login") {
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