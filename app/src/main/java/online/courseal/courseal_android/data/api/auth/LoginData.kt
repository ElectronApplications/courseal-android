package online.courseal.courseal_android.data.api.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginApiRequest(
    val usertag: String,
    val password: String
)

enum class LoginApiError {
    INCORRECT,
    UNKNOWN
}