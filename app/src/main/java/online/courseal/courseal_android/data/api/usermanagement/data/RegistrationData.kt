package online.courseal.courseal_android.data.api.usermanagement.data

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationApiRequest(
    val usertag: String,
    val username: String,
    val password: String
)
enum class RegistrationApiError {
    USER_EXISTS,
    INCORRECT_USERTAG,
    UNKNOWN
}