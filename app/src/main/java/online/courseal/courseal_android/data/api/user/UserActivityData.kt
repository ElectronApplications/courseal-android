package online.courseal.courseal_android.data.api.user

import kotlinx.serialization.Serializable

@Serializable
data class UserActivityApiResponse(
    val usertag: String,
    val activity: List<UserActivityDay>
)

@Serializable
data class UserActivityDay(
    val day: String,
    val xp: Int
)

enum class UserActivityApiError {
    USER_NOT_FOUND,
    UNKNOWN
}