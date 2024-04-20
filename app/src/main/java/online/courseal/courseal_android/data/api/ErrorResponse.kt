package online.courseal.courseal_android.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: ErrorResponseType,
    val message: String?,
    val description: String?
)

@Serializable
enum class ErrorResponseType {
    @SerialName("jwt-invalid") JWT_INVALID,
    @SerialName("refresh-invalid") REFRESH_INVALID,
    @SerialName("user-exists") USER_EXISTS,
    @SerialName("incorrect-usertag") INCORRECT_USERTAG,
    @SerialName("incorrect-login") INCORRECT_LOGIN,
    @SerialName("user-not-found") USER_NOT_FOUND,
    @SerialName("incorrect-password") INCORRECT_PASSWORD,
    @SerialName("course-not-found") COURSE_NOT_FOUND,
    @SerialName("cant-create-courses") CANT_CREATE_COURSES,
}