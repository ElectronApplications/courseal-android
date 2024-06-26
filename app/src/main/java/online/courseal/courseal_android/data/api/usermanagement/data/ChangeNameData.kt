package online.courseal.courseal_android.data.api.usermanagement.data

import kotlinx.serialization.Serializable

@Serializable
data class ChangeNameApiRequest(
    val username: String
)

enum class ChangeNameApiError {
    BAD_REQUEST,
    UNKNOWN
}