package online.courseal.courseal_android.data.api.user.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UserActivityApiResponse(
    val usertag: String,
    val activity: List<UserActivityDay>
)

@Serializable
data class UserActivityDay(
    val day: LocalDate,
    val xp: Int
)

enum class UserActivityApiError {
    USER_NOT_FOUND,
    UNKNOWN
}