package online.courseal.courseal_android.data.api.usermanagement.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordApiRequest(
    @SerialName("old_password") val oldPassword: String,
    @SerialName("new_password") val newPassword: String
)

enum class ChangePasswordApiError {
    BAD_REQUEST,
    PASSWORD_INVALID,
    UNKNOWN
}